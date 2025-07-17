package es.altia.agora.business.escritorio.persistence.manual;

import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.mantenimiento.AccesoModuloUsuarioVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.UserPreferences;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.exception.UsuarioNoValidadoException;
import es.altia.agora.business.util.*;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import es.altia.util.EncriptacionContrasenha;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.DateOperations;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import es.altia.util.jdbc.JdbcOperations;
import java.util.StringTokenizer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.Vector;

public class UsuarioDAO {

	protected static Config m_ConfigTechnical;
	protected static Config m_ConfigError;
	protected static Log m_Log = LogFactory.getLog(UsuarioDAO.class.getName());

    private static UsuarioDAO instance = null;

    protected static String usu_cod;
	protected static String usu_idi;
	protected static String usu_nom;
	protected static String usu_log;
	protected static String usu_pas;
	protected static String usu_blq;

	protected static String apl_cod;
	protected static String apl_nom;
	protected static String apl_exe;
    protected static String apl_ico;

    protected static String aau_apl;
	protected static String aau_usu;

	protected static String org_cod;
	protected static String org_des;
	protected static String org_ico;
	protected static String org_ine;

	protected static String uae_org;
	protected static String uae_ent;
	protected static String uae_apl;
	protected static String uae_usu;

	protected static String ent_cod;
	protected static String ent_nom;
	protected static String ent_org;
	protected static String ent_dtr;

	protected static String uou_uor;
	protected static String uou_usu;
	protected static String uou_org;
	protected static String uou_ent;

	protected static String uor_nom;
        protected static String uor_rex_general;
	protected static String uor_cod;
	protected static String uor_tip;
	protected static String uor_novisreg;
        
	protected static String dep_nom_defecto;
	protected static String dep_cod_defecto;

	protected static String aid_tex;
	protected static String aid_idi;
	protected static String aid_apl;

	protected static String rpu_usu;
	protected static String rpu_org;
	protected static String rpu_ent;
	protected static String rpu_apl;
	protected static String rpu_pro;
	protected static String rpu_tip;

	protected static String ous_usu;
	protected static String ous_org;

	protected static String aae_org;
	protected static String aae_ent;
	protected static String aae_apl;

	protected static String ugo_gru;
	protected static String ugo_usu;
	protected static String ugo_org;
	protected static String ugo_ent;
	protected static String ugo_apl;

	protected static String rpg_org;
	protected static String rpg_ent;
	protected static String rpg_apl;
	protected static String rpg_tip;
	protected static String rpg_gru;
	protected static String rpg_pro;

	protected static String pro_apl;
    protected static String pro_cod;
    protected static String pro_frm;

    protected static String pro_frm_expedientes;
    protected static String pro_frm_padron;

    protected static String organizacion;
	protected static String entidad;
	protected static String aplicacion;
	protected static String ejercicio;
	protected static String gestor;
	protected static String driver;
	protected static String url;
	protected static String usuario;
	protected static String password;
	protected static String fichlog;
	protected static String jndi;

	protected static String gestorD;
	protected static String driverD;
	protected static String urlD;
	protected static String usuarioD;
	protected static String passwordD;
	protected static String fichLogD;
	protected static String jndiD;

    public static String usu_nif;


	protected UsuarioDAO() {
		super();
		//Queremos usar el fichero de	configuracion techserver
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
		//Queremos tener acceso	a los	mensajes de	error	localizados
		m_ConfigError = ConfigServiceHelper.getConfig("error");

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
		usu_idi = m_ConfigTechnical.getString("SQL.A_USU.idioma");
		usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
		usu_log = m_ConfigTechnical.getString("SQL.A_USU.login");
		usu_pas = m_ConfigTechnical.getString("SQL.A_USU.password");
		usu_blq = m_ConfigTechnical.getString("SQL.A_USU.estado");

                apl_cod = m_ConfigTechnical.getString("SQL.A_APL.codigo");
		apl_nom = m_ConfigTechnical.getString("SQL.A_APL.nombre");
		apl_exe = m_ConfigTechnical.getString("SQL.A_APL.url");
		apl_ico = m_ConfigTechnical.getString("SQL.A_APL.icono");

        aau_apl = m_ConfigTechnical.getString("SQL.A_AAU.aplicacion");
		aau_usu = m_ConfigTechnical.getString("SQL.A_AAU.usuario");

        org_cod = m_ConfigTechnical.getString("SQL.A_ORG.codigo");
		org_des = m_ConfigTechnical.getString("SQL.A_ORG.descripcion");
		org_ico = m_ConfigTechnical.getString("SQL.A_ORG.icono");
		org_ine = m_ConfigTechnical.getString("SQL.A_ORG.codINE");

		uae_org = m_ConfigTechnical.getString("SQL.A_UAE.organizacion");
		uae_ent = m_ConfigTechnical.getString("SQL.A_UAE.entidad");
		uae_apl = m_ConfigTechnical.getString("SQL.A_UAE.aplicacion");
		uae_usu = m_ConfigTechnical.getString("SQL.A_UAE.usuario");

		ent_cod = m_ConfigTechnical.getString("SQL.A_ENT.codigo");
		ent_nom = m_ConfigTechnical.getString("SQL.A_ENT.nombre");
		ent_org = m_ConfigTechnical.getString("SQL.A_ENT.organizacion");
		ent_dtr = m_ConfigTechnical.getString("SQL.A_ENT.dirTrabajo");

		uou_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
		uou_usu = m_ConfigTechnical.getString("SQL.A_UOU.usuario");
		uou_org = m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
		uou_ent = m_ConfigTechnical.getString("SQL.A_UOU.entidad");

		uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
                uor_rex_general= m_ConfigTechnical.getString("SQL.A_UOR.rex_xeral");
		uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
		uor_tip = m_ConfigTechnical.getString("SQL.A_UOR.tipo");
		uor_novisreg = m_ConfigTechnical.getString("SQL.A_UOR.noVisRegistro");

		dep_nom_defecto = "DEPARTAMENTO1";
		dep_cod_defecto = "1";

		aid_tex = m_ConfigTechnical.getString("SQL.A_AID.texto");
		aid_idi = m_ConfigTechnical.getString("SQL.A_AID.idioma");
		aid_apl = m_ConfigTechnical.getString("SQL.A_AID.aplicacion");

		rpu_usu = m_ConfigTechnical.getString("SQL.A_RPU.usuario");
		rpu_org = m_ConfigTechnical.getString("SQL.A_RPU.organizacion");
		rpu_ent = m_ConfigTechnical.getString("SQL.A_RPU.entidad");
		rpu_apl = m_ConfigTechnical.getString("SQL.A_RPU.aplicacion");
		rpu_pro = m_ConfigTechnical.getString("SQL.A_RPU.proceso");
		rpu_tip = m_ConfigTechnical.getString("SQL.A_RPU.tipo");

		ous_usu = m_ConfigTechnical.getString("SQL.A_OUS.usuario");
		ous_org = m_ConfigTechnical.getString("SQL.A_OUS.organizacion");

		aae_org = m_ConfigTechnical.getString("SQL.A_AAE.organizacion");
		aae_ent = m_ConfigTechnical.getString("SQL.A_AAE.entidad");
		aae_apl = m_ConfigTechnical.getString("SQL.A_AAE.aplicacion");

		ugo_gru = m_ConfigTechnical.getString("SQL.A_UGO.grupo");
		ugo_usu = m_ConfigTechnical.getString("SQL.A_UGO.usuario");
		ugo_org = m_ConfigTechnical.getString("SQL.A_UGO.organizacion");
		ugo_ent = m_ConfigTechnical.getString("SQL.A_UGO.entidad");
		ugo_apl = m_ConfigTechnical.getString("SQL.A_UGO.aplicacion");

		rpg_org = m_ConfigTechnical.getString("SQL.A_RPG.organizacion");
		rpg_ent = m_ConfigTechnical.getString("SQL.A_RPG.entidad");
		rpg_apl = m_ConfigTechnical.getString("SQL.A_RPG.aplicacion");
		rpg_tip = m_ConfigTechnical.getString("SQL.A_RPG.tipo");
		rpg_gru = m_ConfigTechnical.getString("SQL.A_RPG.grupo");
		rpg_pro = m_ConfigTechnical.getString("SQL.A_RPG.proceso");

		pro_apl = m_ConfigTechnical.getString("SQL.A_PRO.aplicacion");
        pro_cod = m_ConfigTechnical.getString("SQL.A_PRO.codigo");
        pro_frm = m_ConfigTechnical.getString("SQL.A_PRO.formulario");

        pro_frm_expedientes = m_ConfigTechnical.getString("SQL.A_PRO.formulario.tramitacion");
        pro_frm_padron = m_ConfigTechnical.getString("SQL.A_PRO.formulario.gestion");

        organizacion = m_ConfigTechnical.getString("SQL.A_EEA.organizacion");
		entidad = m_ConfigTechnical.getString("SQL.A_EEA.entidad");
		aplicacion = m_ConfigTechnical.getString("SQL.A_EEA.aplicacion");
		ejercicio = m_ConfigTechnical.getString("SQL.A_EEA.ejercicio");
		gestor = m_ConfigTechnical.getString("SQL.A_EEA.gestor");
		driver = m_ConfigTechnical.getString("SQL.A_EEA.driver");
		url = m_ConfigTechnical.getString("SQL.A_EEA.url");
		usuario = m_ConfigTechnical.getString("SQL.A_EEA.usuario");
		password = m_ConfigTechnical.getString("SQL.A_EEA.password");
		fichlog = m_ConfigTechnical.getString("SQL.A_EEA.fichlog");
		jndi = m_ConfigTechnical.getString("SQL.A_EEA.jndi");

        jndiD = m_ConfigTechnical.getString("CON.jndi");
        gestorD = m_ConfigTechnical.getString("CON.gestor");

		driverD = m_ConfigTechnical.getString("CON.driver");
		urlD = m_ConfigTechnical.getString("CON.url");
		usuarioD = m_ConfigTechnical.getString("CON.usuario");
		passwordD = m_ConfigTechnical.getString("CON.password");
		fichLogD = m_ConfigTechnical.getString("CON.fichlog");

        usu_nif = m_ConfigTechnical.getString("SQL.A_USU.nif");
	}

	/**
	* Factory method para el<code>Singelton</code>.
	 * @return La unica instancia de UsuarioDAO.The	only CustomerDAO instance.
	*/
	public static UsuarioDAO getInstance() {
		if (instance == null) {
			synchronized (UsuarioDAO.class) {
				if (instance == null) instance = new UsuarioDAO();
			}
		}
		return instance;
	}

	public UsuarioEscritorioValueObject loadUsuario(UsuarioEscritorioValueObject usuarioEscritorioVO)
            throws TechnicalException, UsuarioNoEncontradoException {
            return loadUsuario(usuarioEscritorioVO, false);
        }
        
	public UsuarioEscritorioValueObject loadUsuario(UsuarioEscritorioValueObject usuarioEscritorioVO, boolean usuarioNoBaja)
            throws TechnicalException, UsuarioNoEncontradoException {

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = oad.getConnection();
            String SQL_BUSCA_USUARIO = "SELECT USU_COD, USU_IDI, USU_NOM "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU "
                + "WHERE USU_LOG = ? AND USU_BLQ=0";
            
            if(usuarioNoBaja) {
                SQL_BUSCA_USUARIO += " AND USU_FBA IS NULL";
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_USUARIO en load Usuario: " + SQL_BUSCA_USUARIO);
            }
            stmt = con.prepareStatement(SQL_BUSCA_USUARIO);
            stmt.setString(1, usuarioEscritorioVO.getLogin());

            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarioEscritorioVO.setIdUsuario(rs.getInt("USU_COD"));
                usuarioEscritorioVO.setIdiomaEsc(rs.getInt("USU_IDI"));
                usuarioEscritorioVO.setNombreUsu(rs.getString("USU_NOM"));
            }

        } catch (ExceptionInInitializerError ei) {
            ei.printStackTrace();
            throw new UsuarioNoEncontradoException("NO SE HA PODIDO OBTENER GlobalNames.ESQUEMA_GENERICO, COMPRUEBE VALOR PROPIEDAD: CON.gestor");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsuarioNoEncontradoException("EXCEPCION CONSULTANDO EL USUARIO");
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        return usuarioEscritorioVO;
    }


    public UsuarioEscritorioValueObject loadUsuarioForCert(String nif)
            throws TechnicalException, UsuarioNoEncontradoException, InternalErrorException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String SQL_BUSCA_USUARIO = "SELECT USU_LOG, USU_COD, USU_IDI, USU_NOM " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU " +
                    "WHERE USU_NIF = ? AND USU_BLQ=0 and usu_fba is null";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_USUARIO en loadUsuarioForCert: " + SQL_BUSCA_USUARIO);
            }
            stmt = con.prepareStatement(SQL_BUSCA_USUARIO);
            stmt.setString(1, nif);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                int i = 1;
                usuarioEscritorioVO.setLogin(rs.getString(i++));
                usuarioEscritorioVO.setIdUsuario(rs.getInt(i++));
                usuarioEscritorioVO.setIdiomaEsc(rs.getInt(i++));
                usuarioEscritorioVO.setNombreUsu(rs.getString(i++));
                usuarioEscritorioVO.setPreferences(new UserPreferences());

                return usuarioEscritorioVO;

            } else {
                throw new UsuarioNoEncontradoException("NO EXISTE EL USUARIO");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException("ERROR DE ACCESO A BASE DE DATOS: " + sqle.getMessage(), sqle);
        } catch (BDException bde) {
            throw new TechnicalException("ERROR AL OBTENER EL ACCESO A BASE DE DATOS: " + bde.getMessage(), bde);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }


    public UsuarioEscritorioValueObject loadUsuarioWithId(int idUsuario)
        throws TechnicalException, UsuarioNoEncontradoException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

			String SQL_BUSCA_USUARIO =
                "SELECT	"
                    + usu_log
                    + ", "
                    + usu_cod
                    + ", "
                    + usu_idi
                    + ", "
                    + usu_nom
                    + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_USU WHERE "
                    + usu_cod
                    + "= ? AND USU_BLQ=0";
			if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_USUARIO en loadusuariowithId: " + SQL_BUSCA_USUARIO);
			stmt = con.prepareStatement(SQL_BUSCA_USUARIO);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                usuarioEscritorioVO.setLogin(rs.getString(usu_log));
                usuarioEscritorioVO.setIdUsuario(rs.getInt(usu_cod));
                usuarioEscritorioVO.setIdiomaEsc(rs.getInt(usu_idi));
                usuarioEscritorioVO.setNombreUsu(rs.getString(usu_nom));
                usuarioEscritorioVO.setPreferences(new UserPreferences());
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            e.printStackTrace();
            throw new UsuarioNoEncontradoException("NO EXISTE EL USUARIO");
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return usuarioEscritorioVO;
        }                    
    }
    
	public Vector getListaUsuarios(String[] params) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Vector resultado = new Vector();
		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
			stmt = con.createStatement();
                        
			String sql =
				"SELECT " + usu_cod + ", " + usu_nom + " FROM " +
                        GlobalNames.ESQUEMA_GENERICO+ "A_USU A_USU";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				GeneralValueObject gVO = new GeneralValueObject();
				gVO.setAtributo("codUsuario", rs.getString(usu_cod));
				gVO.setAtributo("descUsuario", rs.getString(usu_nom));
				resultado.add(gVO);
			}
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (SQLException e) {
			m_Log.error(e.getMessage());
                        e.printStackTrace();
                } catch (BDException e) {
			m_Log.error(e.getMessage());
                        e.printStackTrace();
		} finally {
                    SigpGeneralOperations.devolverConexion(oad, con);
                    return resultado;
		}
		
	}

	   public UsuarioValueObject loadAppEnCurso(UsuarioValueObject usuarioVO)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            String[] param = {"aid_tex"};

            String SQL_BUSCA_APP_EN_CURSO =
                    "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_INITCAP, param) + " AS " + apl_nom
                    + " FROM A_AID WHERE AID_APL = ? AND AID_IDI = ?";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_APP_EN_CURSO: " + SQL_BUSCA_APP_EN_CURSO);
            }
            stmt = con.prepareStatement(SQL_BUSCA_APP_EN_CURSO);
            stmt.setInt(1, usuarioVO.getAppCod());
            stmt.setInt(2, usuarioVO.getIdioma());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarioVO.setApp(rs.getString(apl_nom));
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            
        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return usuarioVO;
        }
    }

	public UsuarioEscritorioValueObject loadApp(UsuarioEscritorioValueObject usuarioEscritorioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		Vector aplic = new Vector();

		try {
			int idioma=usuarioEscritorioVO.getIdiomaEsc();
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

            String [] param={"aid_tex"};
            String SQL_BUSCA_APP = "SELECT " + apl_cod
                    + ", " + oad.funcionCadena(oad.FUNCIONCADENA_INITCAP,  param) + " AS " + apl_nom
                    + ", " + apl_ico + " FROM	A_APL, A_AAU, A_AID WHERE " + apl_cod
					+ "	= " + aau_apl + "	AND " + aau_usu + " =? AND AID_APL=APL_COD AND AID_IDI="
					+ idioma +" ORDER BY "+apl_cod;

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_APP EN CARGA DE ESCRITORIO: " + SQL_BUSCA_APP);
            stmt = con.prepareStatement(SQL_BUSCA_APP);
			stmt.setLong(1, usuarioEscritorioVO.getIdUsuario());
			rs = stmt.executeQuery();
			while (rs.next()) {
				int ac = rs.getInt(apl_cod);
				aplic.addElement(String.valueOf(ac));
                aplic.addElement(rs.getString(apl_nom));
                aplic.addElement(rs.getString(apl_ico));
			}

			usuarioEscritorioVO.setIconos(aplic);

			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
		} finally {
		SigpGeneralOperations.devolverConexion(oad, con);
                return usuarioEscritorioVO;
		}
	}

        public UsuarioEscritorioValueObject buscaCssGeneral(UsuarioEscritorioValueObject usuarioEscritorioVO)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String SQL_BUSCA_APP = "SELECT CSS_RUTA FROM " + GlobalNames.ESQUEMA_GENERICO + "A_CSS WHERE CSS_GENERAL=1";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_css_general EN CARGA DE ESCRITORIO: " + SQL_BUSCA_APP);
            }
            stmt = con.prepareStatement(SQL_BUSCA_APP);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarioEscritorioVO.setCss(rs.getString("CSS_RUTA"));
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return usuarioEscritorioVO;
        }
    }

        public UsuarioValueObject buscaCss(UsuarioValueObject usuarioVO)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};


        try {
            int codorg = usuarioVO.getOrgCod();
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String SQL_BUSCA_APP = "SELECT CSS_RUTA FROM " + GlobalNames.ESQUEMA_GENERICO + "A_CSS WHERE CSS_COD=(SELECT ORG_CSS FROM A_ORG WHERE ORG_COD=" + codorg + ")";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_css_general EN CARGA DE ESCRITORIO: " + SQL_BUSCA_APP);
            }
            stmt = con.prepareStatement(SQL_BUSCA_APP);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarioVO.setCss(rs.getString("CSS_RUTA"));
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return usuarioVO;
        }
    }

	public int comprobarClave(UsuarioEscritorioValueObject usuarioEscritorioVO,String claveAntigua)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		int resultado = 0;

		try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			String SQL = "SELECT "	+ usu_cod + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_USU WHERE " + usu_cod + "=? AND " +
									 usu_pas + "=?";

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL comprobarClave: " + SQL);
                        claveAntigua = EncriptacionContrasenha.getHashSHA_1(claveAntigua);
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, usuarioEscritorioVO.getIdUsuario());
			stmt.setString(2, claveAntigua);
			rs = stmt.executeQuery();
			if (rs.next()) {
				resultado++;
			}

                        SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
                        
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return resultado;
		}
	}

        public boolean esAdmin (UsuarioValueObject usuarioVO) throws TechnicalException{

		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		int resultado = 0;

                try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			// Creamos la select con los parametros adecuados.
			String SQL = "SELECT "	+ ugo_gru + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UGO WHERE " + ugo_usu + "=" + usuarioVO.getIdUsuario()+
                                " AND "+ ugo_ent +"="+ usuarioVO.getEntCod()+ " AND " + ugo_org + "=" + usuarioVO.getOrgCod()+ " AND " + ugo_apl+"="+ usuarioVO.getAppCod();

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL comprobamos si es admin: " + SQL);
			stmt = con.prepareStatement(SQL);
			rs = stmt.executeQuery();
			if (rs.next()) {
				resultado=1;
			}
                        SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return (resultado==1);
		}

        }

	public int modificarClave(UsuarioEscritorioValueObject usuarioEscritorioVO,String claveNueva)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		int resultado = 0;

		try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			String SQL = "UPDATE "+GlobalNames.ESQUEMA_GENERICO+"A_USU SET "	+ usu_pas + "=?" +
			" WHERE " + usu_cod + "=? OR "+ usu_log + "=?";

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL comprobarClave: " + SQL);
                        String claveNuevaSinHash=claveNueva;
                        claveNueva = EncriptacionContrasenha.getHashSHA_1(claveNueva);
			stmt = con.prepareStatement(SQL);
			stmt.setString(1, claveNueva);
			stmt.setLong(2, usuarioEscritorioVO.getIdUsuario());
			stmt.setString(3, usuarioEscritorioVO.getLogin());
			rs = stmt.executeQuery();
			if (rs.next()) {
				resultado++;
			}
                        //Cuando se modifica una contraseña se cambia la rotacion de passwords
                        modificarContrasenhaCaducada( String.valueOf(usuarioEscritorioVO.getIdUsuario()),claveNuevaSinHash);
                        SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
                        
		} catch (Exception e) {
			resultado = 0;
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return resultado;
		}
	}

	public Vector loadOrg(UsuarioValueObject usuarioVO, String filtro)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		Vector org_ent = new Vector();

		try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			String SQL_BUSCA_ORG =
                "SELECT "
                + org_cod
                + ",    "
                + org_des
                + ", "
                + org_ine
                + ", "
                + ent_cod
                + ", "
                + ent_nom
                + ", "
                + ent_dtr
                + ", "
                + org_ico
                + " FROM A_UAE, A_ENT, A_ORG, A_OUS, A_AAE WHERE "
                + uae_usu
                + " = ? AND "
                + uae_apl
                + " = ? AND "
                + uae_org
                + " = "
                + ent_org
                + " AND "
                + uae_ent
                + " = "
                + ent_cod
                + " AND "
                + ent_org
                + " = "
                + org_cod
                + " AND "
                + ous_usu
                + " = "
                + uae_usu
                + " AND "
                + ous_org
                + " = "
                + uae_org
                + " AND "
                + aae_org
                + " = "
                + uae_org
                + " AND "
                + aae_ent
                + " = "
                + uae_ent
                + " AND "
                + aae_apl
                + " = "
                + uae_apl;

            if ((filtro != null) && (!"".equals(filtro))) {
                 StringTokenizer claves = new StringTokenizer(filtro);
                 String clave = "";
                 while (claves.hasMoreTokens()) {
                     clave = claves.nextToken().toUpperCase();
                    //concateno los % para que funcione el like
                    String clave1 ="%" + clave + "%";
                    //concateno los upper para que no destinga entre mayusculas y minusculas
                    String campo1="UPPER(" +org_des+")";
                    String campo2="UPPER(" +ent_nom+")";
                     SQL_BUSCA_ORG += " AND (("+oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRANSLATE, new String[]{
                         oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,new String[] {campo1}),oad.addString("AEIOU"),oad.addString("ÁÉÍÓÚ")})+"LIKE "+
                         oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRANSLATE, new String[]{oad.addString(clave1),oad.addString("AEIOU"),oad.addString("ÁÉÍÓÚ")})+
                         ") OR "+oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRANSLATE, new String[]{
                         oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,new String[] {campo2}),oad.addString("AEIOU"),oad.addString("ÁÉÍÓÚ")})+"LIKE "+
                         oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRANSLATE, new String[]{oad.addString(clave1),oad.addString("AEIOU"),oad.addString("ÁÉÍÓÚ")})+
                         ") ";
                 }
            }

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_ORG: " + SQL_BUSCA_ORG);
			stmt = con.prepareStatement(SQL_BUSCA_ORG);
			stmt.setInt(1, usuarioVO.getIdUsuario());
			stmt.setInt(2, usuarioVO.getAppCod());

			rs = stmt.executeQuery();
			int resultados = 0;
			while (rs.next()) {
				org_ent.addElement(rs.getString(org_cod));
				org_ent.addElement(rs.getString(org_des));
				org_ent.addElement(rs.getString(ent_cod));
				org_ent.addElement(rs.getString(ent_nom));
				org_ent.addElement(rs.getString(ent_dtr));
				org_ent.addElement(rs.getString(org_ico));
				org_ent.addElement(rs.getString(org_ine));
				resultados++;
			}
			m_Log.debug("Organizaciones recuperadas : " + resultados);
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);                                                    
			return org_ent;
		}
	}

    public int consultarCodigo(UsuarioValueObject usuarioVO, String frm)
        throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] =
            { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };
        int resultado = 0;

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            // Creamos la select con los parametros adecuados.
            String SQL = "SELECT " + pro_cod + " FROM A_PRO WHERE " +
                                     pro_apl + "=? AND " + pro_frm + "=?";

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + SQL);
            stmt = con.prepareStatement(SQL);
            int i = 1;
            stmt.setInt(i++, usuarioVO.getAppCod());
            stmt.setString(i++, frm);
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=1;
                resultado = rs.getInt(i++);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);

        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return resultado;
        }
    }

    public String soloConsultarExpedientes(UsuarioValueObject usuarioVO)
        throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] =
            { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

        String resultado = "si";

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            int pro_cod = consultarCodigo(usuarioVO,pro_frm_expedientes);
            // Creamos la select con los parametros adecuados.
            String SQL = "SELECT " + rpg_pro + " FROM A_RPG,A_UGO WHERE " +
                                     ugo_usu + "=" + usuarioVO.getIdUsuario() + " AND " +
                                     ugo_org + "=" + usuarioVO.getOrgCod() + " AND " +
                                     ugo_ent + "=" + usuarioVO.getEntCod() + " AND " +
                                     ugo_apl + "=" + usuarioVO.getAppCod() + " AND " +
                                     ugo_org + "=" + rpg_org + " AND " +
                                     ugo_ent + "=" + rpg_ent + " AND " + ugo_apl + "=" + rpg_apl +
                                     " AND " + ugo_gru + "=" + rpg_gru + " AND " + rpg_pro +
                                     "=" + pro_cod + " AND " + rpg_tip + "=1";

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + SQL);
            stmt = con.prepareStatement(SQL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resultado = "no";

            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return resultado;
        }
    }

	public String soloConsultarPadron(UsuarioValueObject usuarioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		String resultado = "si";

		try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
            int pro_cod = consultarCodigo(usuarioVO,pro_frm_padron);
			// Creamos la select con los parametros adecuados.
			String SQL = "SELECT " + rpg_pro + " FROM A_RPG,A_UGO WHERE " +
									 ugo_usu + "=" + usuarioVO.getIdUsuario() + " AND " +
									 ugo_org + "=" + usuarioVO.getOrgCod() + " AND " +
									 ugo_ent + "=" + usuarioVO.getEntCod() + " AND " +
									 ugo_apl + "=" + usuarioVO.getAppCod() + " AND " +
                                     ugo_org + "=" + rpg_org + " AND " +
									 ugo_ent + "=" + rpg_ent + " AND " + ugo_apl + "=" + rpg_apl +
									 " AND " + ugo_gru + "=" + rpg_gru + " AND " + rpg_pro +
									 "=" + pro_cod + " AND " + rpg_tip + "=1";

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + SQL);
            stmt = con.prepareStatement(SQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				resultado = "no";
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			//Aquí se pueden lanzar	TechnicalException que no se capturan.
			try {
				oad.devolverConexion(con);
			} catch (BDException bde) {
				bde.getMensaje();
			}
            return resultado;
		}
	}

	public Vector loadUnidadOrg(UsuarioValueObject usuarioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector vecUOR = new Vector();
		int aplicacion;

		try {
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());
			con = oad.getConnection();
			

			// Creamos la select con los parametros adecuados.
			String SQL_BUSCA_UNIDAD_ORG =
					"SELECT "
						+ uor_cod
						+ ", "
						+ uor_nom
                                                + ", "
						+ uor_rex_general
						+ " FROM A_UOR, " +GlobalNames.ESQUEMA_GENERICO+"A_UOU,"+GlobalNames.ESQUEMA_GENERICO+"A_ORG  WHERE "
						+ uou_org
						+ "	= ? AND "
						+ uou_ent
						+ "	= ? AND "
						+ uou_usu
						+ "	= ? AND "
						+ uor_tip
						+ "	= ? AND "
						+ uou_uor
						+ "	= "
						+ uor_cod
						+ "	AND "
						+ uou_org
						+ "	= "
						+ org_cod ;

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_UNIDAD_ORG:	" + SQL_BUSCA_UNIDAD_ORG);

            Config common = ConfigServiceHelper.getConfig("common");

            int APP_REGISTRO_ENTRADA = common.getInt("APP_REGISTRO_ENTRADA");
            int APP_REGISTRO_SALIDA = common.getInt("APP_REGISTRO_SALIDA");
			if ((usuarioVO.getAppCod() == APP_REGISTRO_ENTRADA)
                ||(usuarioVO.getAppCod() == APP_REGISTRO_SALIDA))
				aplicacion = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
			else
				aplicacion = usuarioVO.getAppCod();
			
            if(m_Log.isDebugEnabled()) m_Log.debug("Valores pasados a la query:	" + usuarioVO.getOrgCod() + "-" + usuarioVO.getEntCod() + "-" +
					usuarioVO.getIdUsuario() + "-" + aplicacion);

			stmt = con.prepareStatement(SQL_BUSCA_UNIDAD_ORG);
			stmt.setInt(1, usuarioVO.getOrgCod());
			stmt.setInt(2, usuarioVO.getEntCod());
			stmt.setInt(3, usuarioVO.getIdUsuario());
            stmt.setInt(4, aplicacion);
			rs = stmt.executeQuery();
			while (rs.next()) {
				vecUOR.addElement(rs.getString(uor_cod));
				vecUOR.addElement(rs.getString(uor_nom));
                                vecUOR.addElement(rs.getString(uor_rex_general));
			}
			m_Log.debug(vecUOR);
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return vecUOR;
		}
	}

        /**
         * Método similar al anterior que recupera las unidades organizativas sobre las que
         * tiene permiso un usuario.
         * @param usuarioVO
         * @return Vector
         * @throws es.altia.common.exception.TechnicalException
         */
        public Vector<UORDTO> cargarUnidadOrganizativa(UsuarioValueObject usuarioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector vecUOR = new Vector();

                try {
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());
			con = oad.getConnection();
                        int tipoUord = 0;
                        Config common = ConfigServiceHelper.getConfig("common");
                        int APP_REGISTRO_ENTRADA = common.getInt("APP_REGISTRO_ENTRADA");
                        int APP_REGISTRO_SALIDA = common.getInt("APP_REGISTRO_SALIDA");
                        if ((usuarioVO.getAppCod() == APP_REGISTRO_ENTRADA) ||(usuarioVO.getAppCod() == APP_REGISTRO_SALIDA))
                            tipoUord = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
                        else tipoUord = usuarioVO.getAppCod();
                        String sql = "SELECT UOR_COD,UOR_NOM,UOR_REX_GENERAL,UOR_PAD"
                                + " FROM A_UOR, " +GlobalNames.ESQUEMA_GENERICO+"A_UOU,"+GlobalNames.ESQUEMA_GENERICO+"A_ORG  WHERE "
                                + uou_org
                                + "	= " + usuarioVO.getOrgCod() +  " AND "
                                + uou_ent
                                + "	= " + usuarioVO.getEntCod() + " AND "
                                + uou_usu
                                + "	= " + usuarioVO.getIdUsuario() + " AND "
                                + uor_tip
                                + "	= " + tipoUord + " AND "
                                + uou_uor
                                + "	= "
                                + uor_cod
                                + "	AND "
                                + uou_org
                                + "	= "
                                + org_cod;

                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

                        while (rs.next()) {
                            UORDTO uor = new UORDTO();
                            uor.setUor_cod(rs.getString("UOR_COD"));
                            uor.setUor_nom(rs.getString("UOR_NOM"));
                            String uorPad = rs.getString("UOR_PAD");
                            uor.setUor_pad(uorPad);
                            uor.setUor_rexistro_xeral(rs.getString("UOR_REX_GENERAL"));
                            vecUOR.addElement(uor);
			}

			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (BDException e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return vecUOR;
		}
	}

	public String loadURL(UsuarioValueObject usuarioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String params[] =
			{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };
		String url = null;

		try {

			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
			String SQL_BUSCA_URL =
				"SELECT " + apl_exe + " FROM A_APL WHERE " + apl_cod + " = ?";
            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_URL: " + SQL_BUSCA_URL);
			stmt = con.prepareStatement(SQL_BUSCA_URL);
			stmt.setInt(1, usuarioVO.getAppCod());
			rs = stmt.executeQuery();
			while (rs.next()) {
				url = rs.getString(apl_exe);
			}
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (BDException e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return url;
		}
	}

	public UsuarioValueObject loadMantenimiento(UsuarioValueObject usuarioVO)
		throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());
			con = oad.getConnection();

			String SQL_BUSCA_MANTENIMIENTO =
				"SELECT "
					+ rpu_pro
					+ ", "
					+ rpu_tip
					+ " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_RPU WHERE "
					+ rpu_usu
					+ " = ? AND "
					+ rpu_org
					+ " = ? AND "
					+ rpu_ent
					+ " = ? AND "
					+ rpu_apl
					+ " = ?";

			stmt = con.prepareStatement(SQL_BUSCA_MANTENIMIENTO);
			stmt.setInt(1, usuarioVO.getIdUsuario());
			stmt.setInt(2, usuarioVO.getOrgCod());
			stmt.setInt(3, usuarioVO.getEntCod());
			stmt.setInt(4, usuarioVO.getAppCod());
			rs = stmt.executeQuery();
			while (rs.next()) {
				// procesos	consultas rpu_pro=12 y rpu_pro=22, entrada y salida respectivamente, activados con rpu_tip=1.
				if ((rs.getInt(rpu_pro) == 11) && (rs.getInt(rpu_tip) == 1))
					usuarioVO.setMantenerEntrada(false);
				if ((rs.getInt(rpu_pro) == 21) && (rs.getInt(rpu_tip) == 1))
					usuarioVO.setMantenerSalida(false);
			}
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return usuarioVO;
		}
	}

  public int getNumeroIdiomas(UsuarioEscritorioValueObject usuarioEscritorioVO) throws TechnicalException {
      AdaptadorSQLBD oad = null;
      Connection con = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      int numIdiomas = 0;
      String params[] =
          { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

      try {
          oad = new AdaptadorSQLBD(params);
          con = oad.getConnection();

          String sql= "SELECT count(*) from A_IDI";
          if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          if (rs.next()) {
              numIdiomas = rs.getInt(1);
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);
      } catch (Exception e) {
          m_Log.error(e.getMessage());
          e.printStackTrace();
      } finally {
          SigpGeneralOperations.devolverConexion(oad, con);
          return numIdiomas;
      }
  }

    public String[] loadParametrosConexion(UsuarioValueObject uvo) throws TechnicalException {
        
        String[] parametros = new String[7];

        ParametrosBDVO parametrosBD = (ParametrosBDVO)CacheDatosFactoria.getImplParametrosBD().getDatoClaveUnica(String.valueOf(uvo.getOrgCod()),
                String.valueOf(uvo.getEntCod()),String.valueOf(uvo.getAppCod()));

        if (parametrosBD!=null) {
            parametros[0] = (parametrosBD.getGestor() == null || "".equals(parametrosBD.getGestor().trim())?gestorD:parametrosBD.getGestor());
            parametros[1] = parametrosBD.getDriver();
            parametros[2] = parametrosBD.getUrl();
            parametros[3] = parametrosBD.getUsuario();
            parametros[4] = parametrosBD.getPassword();
            parametros[5] = parametrosBD.getFichlog();
            parametros[6] = parametrosBD.getJndi();       
        }
        return parametros;
    }
	
    public int getGrupo(UsuarioValueObject uvo) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int grupo = 0;
		String params[] =
		{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			String sql = "SELECT DISTINCT A_GRU.GRU_COD FROM A_GRU, A_UGO WHERE " +
			"A_GRU.GRU_NOM = 'ADMINISTRADOR' AND "+
			"A_GRU.GRU_COD = A_UGO.UGO_GRU AND "+
			"A_UGO.UGO_USU = "+ uvo.getIdUsuario() +" AND "+
			"A_UGO.UGO_APL = "+ uvo.getAppCod();

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				grupo = rs.getInt(1);
			}
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return grupo;
		}
	}

	public String getNombre(String login) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String nombre = "";
		String params[] =
		{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			// Creamos la select con los parametros adecuados.
			String sql = "SELECT DISTINCT A_USU.USU_NOM "+
			"FROM A_USU WHERE " +
			"A_USU.USU_LOG = '"+ login +"'";

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				nombre = rs.getString(1);
			}
			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return nombre;
		}
	}

	public int bloquearUsuario(UsuarioEscritorioValueObject uvo, int estado) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		int resultado = -1;
		String params[] =
		{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			String sql = "UPDATE A_USU SET USU_BLQ = "+ estado +" "+
			"WHERE USU_LOG = '"+ uvo.getLogin() +"'";

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
			stmt = con.prepareStatement(sql);

			resultado = stmt.executeUpdate();
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return resultado;
		}
	}

	public int getBloqueado(UsuarioEscritorioValueObject uvo) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = -1;
		String params[] =
		{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			// Creamos la select con los parametros adecuados.
			String sql = "SELECT USU_BLQ  FROM A_USU "+
			"WHERE USU_LOG = '"+ uvo.getLogin() +"'";

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			if (rs.next()) {
				resultado = rs.getInt(1);
			}

                        SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return resultado;
		}
	}

        public int getEliminado(UsuarioEscritorioValueObject uvo) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = -1;
		String params[] =
		{ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();

			// Creamos la select con los parametros adecuados.
			String sql = "SELECT USU_FBA  FROM A_USU "+
			"WHERE USU_LOG = '"+ uvo.getLogin() +"' AND USU_FBA IS NULL";

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			if (rs.next()) {
				resultado = 0;
			}

			SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
			return resultado;
		}
	}

    public boolean validarUsuarioAlta(UsuarioEscritorioValueObject userDesktop) throws UsuarioNoValidadoException {
        return true;
    }

    public boolean validarUsuario(String login, String password) throws UsuarioNoValidadoException, TechnicalException {

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
                    con = oad.getConnection();
			String SQL_BUSCA_USUARIO =
				"SELECT	* FROM " +  GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE " + usu_log + "= ?";

            if (password != null){
                try{
                    password = EncriptacionContrasenha.getHashSHA_1(password);
                }catch(Exception e){
                    e.printStackTrace();
                }

                SQL_BUSCA_USUARIO = SQL_BUSCA_USUARIO + " AND USU_PAS = ?";
            }

			if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_USUARIO: " + SQL_BUSCA_USUARIO);
			stmt = con.prepareStatement(SQL_BUSCA_USUARIO);
			stmt.setString(1, login);
			if (password != null) {
				stmt.setString(2, password);
			}
			rs = stmt.executeQuery();

            boolean validado = rs.next();
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            m_Log.debug("EL RESULTADO DE LA VALIDACION ES: " + validado);
            return validado;

        } catch (SQLException sqle) {
            throw new UsuarioNoValidadoException("Administracion.InsertarUsuario.ErrorValidacion.UsuarioNoExiste");
        } catch (BDException bde) {
            throw new UsuarioNoValidadoException("Administracion.InsertarUsuario.ErrorValidacion.UsuarioNoExiste");
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

    /**
     * Recupera el id del usuario con un determinado login
     * @param login: Login del usuario
     * @param params: Parámetros de conexión
     * @return 0 si no se puede recuperar y distinto de cero en caso contrario
     */
    public int getIdUsuario(String login,String[] params)
    throws  TechnicalException{
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int idUsuario = 0;

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            sql = "SELECT USU_COD FROM " +  GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_LOG='" + login + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt= conexion.createStatement();
            rs = stmt.executeQuery(sql);

            if(rs!=null)
                m_Log.debug("Resultset no nulo");
            else
                m_Log.debug("Resultset nulo");
            while(rs.next()) {
                int i=0;
                idUsuario = rs.getInt("USU_COD");
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            m_Log.debug(">>> UsuarioDAO - getIdUsuario: " + idUsuario);
            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            } finally {
                    SigpGeneralOperations.devolverConexion(abd, conexion);
                    return idUsuario;
            }        

    }// getIdUsuario

    /**
     * Recupera el nombre completo de un usuario
     * @param codUsuario: Código del usuario
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
     public String getNombreUsuario(String[] params,int codUsuario)
            throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String nombre = "";
        String sql = "";

	try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + usu_nom
                + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE "
                + usu_cod + " ='" + codUsuario + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug("existeUsuarioCodigo: " + sql);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                nombre = rs.getString(usu_nom);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar nombre del usuario: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return nombre;
        }

        
    }




    /**
     * Permite cargar un usuario a partir de su login
     * @param nif: Nif o login del usuario
     * @return UsuarioEscritorioValueObject
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException
     * @throws es.altia.util.exceptions.InternalErrorException
     */
    public UsuarioEscritorioValueObject loadUsuarioForDniE(String nif)
            throws TechnicalException, UsuarioNoEncontradoException, InternalErrorException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            // Creamos la select con los parametros adecuados.
            String SQL_BUSCA_USUARIO = "SELECT USU_LOG, USU_COD, USU_IDI, USU_NOM " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU " +
                    "WHERE USU_NIF = ? AND USU_BLQ=0 AND USU_FBA IS NULL";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL_BUSCA_USUARIO en loadUsuarioForDNIe: " + SQL_BUSCA_USUARIO);
            }
            stmt = con.prepareStatement(SQL_BUSCA_USUARIO);
            stmt.setString(1, nif);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                int i = 1;
                usuarioEscritorioVO.setLogin(rs.getString(i++));
                usuarioEscritorioVO.setIdUsuario(rs.getInt(i++));
                usuarioEscritorioVO.setIdiomaEsc(rs.getInt(i++));
                usuarioEscritorioVO.setNombreUsu(rs.getString(i++));
                usuarioEscritorioVO.setPreferences(new UserPreferences());

                return usuarioEscritorioVO;

            } else {
                throw new UsuarioNoEncontradoException("NO EXISTE EL USUARIO");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException("ERROR DE ACCESO A BASE DE DATOS: " + sqle.getMessage(), sqle);
        } catch (BDException bde) {
            throw new TechnicalException("ERROR AL OBTENER EL ACCESO A BASE DE DATOS: " + bde.getMessage(), bde);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }



     /**
     * Permite obtener la fecha de creacion de la contraseña
     * @param UsuarioEscritorioValueObject
     * @return Calendar
     * @throws es.altia.common.exception.TechnicalException
     */
    public Calendar getFechaAltaPasswordReciente(UsuarioEscritorioValueObject usuario)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Calendar fechaAltaPasswordReciente = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
           
            String sql =" SELECT DISTINCT A_USU_ROTACION_PASS.FEC_ALTA "
                    + " FROM A_USU_ROTACION_PASS, A_USU "
                    + " WHERE A_USU_ROTACION_PASS.USU_COD = ' " +  usuario.getIdUsuario()  + " ' "
                    + " AND A_USU.USU_PAS = A_USU_ROTACION_PASS.USU_PASS order by A_USU_ROTACION_PASS.FEC_ALTA DESC" ;
            
            //Ordenamos inversamente por si se diera el caso de que hubiese 2 contraseñas iguales en la tabla para el mismo usuario
            //Si modifica el admin puede darse

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
            }
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                fechaAltaPasswordReciente = DateOperations.toCalendar(rs.getTimestamp("FEC_ALTA"));
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally { 
            SigpGeneralOperations.devolverConexion(oad, con);
            return fechaAltaPasswordReciente;
        }
    }

     /**
     * Permite obtener la fecha del ultimo acceso a la aplicacion para el usuario.
     * 
     * @param idUsuario
     * @return Calendar
     * @throws es.altia.common.exception.TechnicalException
     */
    public Calendar getFechaUltimoAcceso(Integer idUsuario)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Calendar fechaUltimoAcceso = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
           
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT MAX(FECHA_ACCESO) AS FECHA_ULTIMO_ACCESO ")
               .append(" FROM AUDITORIA_ACCESO_MODULOS ")
               .append(" WHERE ID_USUARIO = ? ")
               .append("   AND ID_ORGANIZACION IS NULL ")
               .append(String.format("   AND ID_APLICACION = %d ", ConstantesDatos.CODIGO_ACCESO_FLEXIA));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql.toString());
                m_Log.debug("ID_USUARIO = " + idUsuario);
            }
            
            stmt = con.prepareStatement(sql.toString());
            stmt.setInt(1, idUsuario);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                fechaUltimoAcceso = DateOperations.toCalendar(rs.getTimestamp("FECHA_ULTIMO_ACCESO"));
            }
            
        } catch (Exception e) {
            m_Log.error(e.getMessage(), e);
        } finally { 
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
            return fechaUltimoAcceso;
        }
    }
    
    /**
     * Obtiene los accesos a los modulos que ha realizado el usuario.
     * 
     * @param idOrganizacion
     * @param idAplicacion
     * @param loginUsuario
     * @param nombreUsuario
     * @param operacionFecha
     * @param fechaInicio
     * @param fechaFin
     * @return
     * @throws TechnicalException 
     */
    public List<AccesoModuloUsuarioVO> buscarAccesosModulos(
            Integer idOrganizacion, Integer idAplicacion, String loginUsuario,
            String nombreUsuario, DateOperations.OPERACION_FECHA_BD operacionFecha,
            Calendar fechaInicio, Calendar fechaFin)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        List<AccesoModuloUsuarioVO> listaAccesos = new ArrayList<AccesoModuloUsuarioVO>();
        AccesoModuloUsuarioVO accesoModulo = null;
        
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            boolean whereInicializado = false;
            
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT AUDITORIA_ACCESO_MODULOS.ID_ORGANIZACION, A_ORG.ORG_DES, AUDITORIA_ACCESO_MODULOS.ID_USUARIO, A_USU.USU_LOG, A_USU.USU_NOM, AUDITORIA_ACCESO_MODULOS.ID_APLICACION, A_APL.APL_NOM, AUDITORIA_ACCESO_MODULOS.FECHA_ACCESO ")
               .append(" FROM AUDITORIA_ACCESO_MODULOS AUDITORIA_ACCESO_MODULOS ")
               .append("   LEFT JOIN A_ORG ON A_ORG.ORG_COD = AUDITORIA_ACCESO_MODULOS.ID_ORGANIZACION ")
               .append("   LEFT JOIN A_USU ON A_USU.USU_COD = AUDITORIA_ACCESO_MODULOS.ID_USUARIO ")
               .append("   LEFT JOIN A_APL ON A_APL.APL_COD = AUDITORIA_ACCESO_MODULOS.ID_APLICACION ");

            // WHERE
            if (idOrganizacion != null) {
                whereInicializado = JdbcOperations.anadirFiltroWhere(sql, whereInicializado);
                sql.append(" AUDITORIA_ACCESO_MODULOS.ID_ORGANIZACION = ? ");
            }
            
            if (loginUsuario != null && !loginUsuario.isEmpty()) {
                whereInicializado = JdbcOperations.anadirFiltroWhere(sql, whereInicializado);
                sql.append(oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{"A_USU.USU_LOG"}));
                sql.append(" LIKE ? ");
            }
            
            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                whereInicializado = JdbcOperations.anadirFiltroWhere(sql, whereInicializado);
                sql.append(oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_UPPER, new String[]{"A_USU.USU_NOM"}));
                sql.append(" LIKE ? ");
            }
            
            if (idAplicacion != null) {
                whereInicializado = JdbcOperations.anadirFiltroWhere(sql, whereInicializado);
                sql.append(" AUDITORIA_ACCESO_MODULOS.ID_APLICACION = ? ");
            }
            
            if (operacionFecha != null && operacionFecha != DateOperations.OPERACION_FECHA_BD.NADA) {
                whereInicializado = JdbcOperations.anadirFiltroWhere(sql, whereInicializado);
                sql.append(JdbcOperations.construirFiltroFecha(oad, operacionFecha, "AUDITORIA_ACCESO_MODULOS.FECHA_ACCESO", true));
            }
            
            //Order By
            sql.append(" ORDER BY AUDITORIA_ACCESO_MODULOS.FECHA_ACCESO DESC ");
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql.toString());
                m_Log.debug("ID_ORGANIZACION = " + idOrganizacion);
                m_Log.debug("USU_LOG = " + loginUsuario);
                m_Log.debug("USU_NOM = " + nombreUsuario);
                m_Log.debug("ID_APLICACION = " + idAplicacion);
                m_Log.debug("operacionFecha = " + operacionFecha);
                m_Log.debug("FechaInicio = " + fechaInicio);
                m_Log.debug("FechaFin = " + fechaFin);
            }
            
            stmt = con.prepareStatement(sql.toString());
            
            // Asignacion de variables
            int index = 1;
            if (idOrganizacion != null) {
                stmt.setInt(index++, idOrganizacion);    
            }
            
            if (loginUsuario != null && !loginUsuario.isEmpty()) {
                stmt.setString(index++
                        , JdbcOperations.convertirALike(loginUsuario.toUpperCase(), JdbcOperations.LIKE_OPERATIONS.AMBOS));    
            }
            
            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                stmt.setString(index++
                        , JdbcOperations.convertirALike(nombreUsuario.toUpperCase(), JdbcOperations.LIKE_OPERATIONS.AMBOS));    
            }
            
            if (idAplicacion != null) {
                stmt.setInt(index++, idAplicacion);    
            }

            if (operacionFecha != null && operacionFecha != DateOperations.OPERACION_FECHA_BD.NADA) {
                stmt.setTimestamp(index++, DateOperations.toTimestamp(fechaInicio));
                
                if (operacionFecha == DateOperations.OPERACION_FECHA_BD.ENTRE) {
                    stmt.setTimestamp(index++, DateOperations.toTimestamp(fechaFin));
                }
            }
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                accesoModulo = new AccesoModuloUsuarioVO();
                if (rs.getObject("ID_ORGANIZACION") != null) {
                    accesoModulo.setIdOrganizacion(rs.getInt("ID_ORGANIZACION"));
                }
                
                if (rs.getObject("ID_USUARIO") != null) {
                    accesoModulo.setIdUsuario(rs.getInt("ID_USUARIO"));
                }
                
                if (rs.getObject("ID_APLICACION") != null) {
                    accesoModulo.setIdAplicacion(rs.getInt("ID_APLICACION"));
                }
                
                accesoModulo.setNombreOrganizacion(rs.getString("ORG_DES"));
                accesoModulo.setLoginUsuario(rs.getString("USU_LOG"));
                accesoModulo.setNombreUsuario(rs.getString("USU_NOM"));
                accesoModulo.setNombreAplicacion(rs.getString("APL_NOM"));
                accesoModulo.setFechaHora(rs.getTimestamp("FECHA_ACCESO"));
                
                listaAccesos.add(accesoModulo); 
            }            
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally { 
            SigpGeneralOperations.devolverConexion(oad, con);
            return listaAccesos;
        }
    }
    
     /**
     * Permite modifcar una contraseña caducada
     * @param String, String
     * @return boolean
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean modificarContrasenhaCaducada(String login, String password) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};

        int resultado = 0;
        int numeroPass = 0;
        int codPass = 0;
        String SQL = "";

        Config ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        String passwordRotacion = ConfigTechnical.getString("password.rotacion.numero");

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            //Consulta para determinar si el numero de registros es mayor que ¿n?
            SQL = " SELECT COUNT(*) AS NUM_USU_COD "
                    + " FROM A_USU_ROTACION_PASS "
                    + " WHERE A_USU_ROTACION_PASS.USU_COD = '" + login + "'";

            stmt = con.prepareStatement(SQL);
            rs = stmt.executeQuery();

            if (rs.next()) {
                numeroPass = rs.getInt("NUM_USU_COD");
            }

            //Consulta para obtener el codigo de password mayor 
            SQL = " SELECT MAX(A_USU_ROTACION_PASS.COD_PASS) AS COD_PASS "
                    + " FROM A_USU_ROTACION_PASS ";

            stmt = con.prepareStatement(SQL);
            rs = stmt.executeQuery();

            if (rs.next()) {
                codPass = rs.getInt("COD_PASS");
            }

            //comprobar si dicho usuario ya tiene asociadas ?n? claves
            if (numeroPass >= Integer.parseInt(passwordRotacion)) {
                this.eliminarPasswordAntigua(login, con);
            }
            this.actualizarPassword(password, login, codPass, con);

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.commit(oad, con);
        } catch (Exception e) {
            SigpGeneralOperations.rollBack(oad, con);
            resultado = -1;
        } finally {            
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return (resultado == 1);
    }


    public boolean existeContrasenha(String login, String password) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        List listaPassword = new ArrayList();
        boolean resultado = false;
        
        Config ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        String passwordRotacion = ConfigTechnical.getString("password.rotacion.numero");


        try {
            String hashContrasenha = EncriptacionContrasenha.getHashSHA_1(password);
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            //Busca en la tabla de rotacion si existe la contraseña
            String SQL = " SELECT A_USU_ROTACION_PASS.USU_PASS "
                    + " FROM A_USU_ROTACION_PASS "
                    + " WHERE A_USU_ROTACION_PASS.USU_COD =  '" + login + "' order by A_USU_ROTACION_PASS.COD_PASS DESC";

            if (m_Log.isDebugEnabled()) { 
                m_Log.debug("SQL: " + SQL);
            }
            stmt = con.prepareStatement(SQL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String usuPass = rs.getString("USU_PASS");
                listaPassword.add(usuPass);
            }

            if (listaPassword.size() > 0) {
                for (int i = 0; i < listaPassword.size(); i++) {
                    if (i < Integer.parseInt(passwordRotacion)) {
                        String pass = String.valueOf(listaPassword.get(i));
                        if (pass.equalsIgnoreCase(hashContrasenha)) {
                            resultado = true;
                        }
                    }

                }
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
           SigpGeneralOperations.devolverConexion(oad, con);
            return resultado;
        }
    }



    private int eliminarPasswordAntigua(String login, Connection con) throws TechnicalException {
        PreparedStatement stmt = null;
        int resultado = 0;

        try {
            //Elimina el registro mas antiguo de la tabla rotacion
            String SQL = " DELETE FROM A_USU_ROTACION_PASS "
                    + " WHERE A_USU_ROTACION_PASS.USU_COD = '" + login + "' "
                    + " AND A_USU_ROTACION_PASS.FEC_ALTA IN "
                    + "(SELECT MIN(FEC_ALTA) "
                    + " FROM  A_USU_ROTACION_PASS) ";
            m_Log.debug(SQL);
            stmt = con.prepareStatement(SQL);
            resultado = stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            resultado = -1;
            m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            return resultado;
        }
    }

    private int actualizarPassword(String password, String login, int codPass, Connection con) throws TechnicalException {
        PreparedStatement stmt = null;
        int resultado = 0;

        try {
            String hashContrasenha = EncriptacionContrasenha.getHashSHA_1(password);

            //Actualiza la contraseña de la tabla de usuarios
            String SQL = "UPDATE A_USU SET A_USU.USU_PAS = '" + hashContrasenha + "' "
                    + " WHERE A_USU.USU_COD = '" + login + "'";
            m_Log.debug(SQL);
            stmt = con.prepareStatement(SQL);
            resultado = stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);

            this.insertarPasswordRotacion(password, login, codPass, con);

        } catch (Exception e) {
            m_Log.error(e.getMessage());
            resultado = -1;
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            return resultado;
        }
    }

    private int insertarPasswordRotacion(String password, String login, int codPass, Connection con) throws TechnicalException {
        PreparedStatement stmt = null;
        int resultado = 0;
        Calendar fechaActual = Calendar.getInstance();

        try {
            String hashContrasenha = EncriptacionContrasenha.getHashSHA_1(password);

            //Creamos la select con los parametros adecuados.
            codPass = codPass + 1;

            //inserta un nuevo registro en la tabla de rotacion
            String SQL = "INSERT INTO A_USU_ROTACION_PASS (COD_PASS, FEC_ALTA, USU_COD, USU_PASS) "
                    + " VALUES ('" + codPass + "',?,'" + login + "','" + hashContrasenha + "') ";

            m_Log.debug(SQL);
            stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1, DateOperations.toTimestamp(fechaActual));
            resultado = stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);


        } catch (Exception e) {
            m_Log.error(e.getMessage());
            resultado = -1;
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            return resultado;
        }
    }

    /**
     * Inserta un registro en la tabla AUDITORIA_ACCESO_MODULOS de auditoria de acceso a los modulos.
     * 
     * @param idOrganizacion
     * @param idAplicacion
     * @param idUsuario
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public int insertarAuditoriaAccesoModulo(Integer idOrganizacion, Integer idAplicacion, Integer idUsuario)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        PreparedStatement stmt = null;
        int resultado = 0;
        Calendar fechaActual = Calendar.getInstance();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            StringBuilder sql = new StringBuilder();

            if (gestorD.equalsIgnoreCase(ConstantesDatos.ORACLE)) {
                sql.append(" INSERT INTO AUDITORIA_ACCESO_MODULOS (ID, ID_ORGANIZACION, ID_USUARIO, ID_APLICACION, FECHA_ACCESO) ")
                        .append(" VALUES (SEQ_AUDITORIA_ACCESO_MODULOS.NEXTVAL, ?, ?, ?, ?) ");
            } else if (gestorD.equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {
                sql.append(" INSERT INTO AUDITORIA_ACCESO_MODULOS (ID_ORGANIZACION, ID_USUARIO, ID_APLICACION, FECHA_ACCESO) ")
                        .append(" VALUES (?, ?, ?, ?) ");
            } else {
                throw new TechnicalException("Gestor de base de datos no soportado: " + gestorD);
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql.toString());
                m_Log.debug("ID_ORGANIZACION = " + idOrganizacion);
                m_Log.debug("ID_USUARIO = " + idUsuario);
                m_Log.debug("ID_APLICACION = " + idAplicacion);
                m_Log.debug("FECHA_ACCESO = " + fechaActual.toString());
            }
            
            stmt = con.prepareStatement(sql.toString());
            stmt.setObject(1, idOrganizacion, java.sql.Types.INTEGER);
            stmt.setObject(2, idUsuario, java.sql.Types.INTEGER);
            stmt.setObject(3, idAplicacion, java.sql.Types.INTEGER);
            stmt.setObject(4, DateOperations.toTimestamp(fechaActual), java.sql.Types.TIMESTAMP);

            resultado = stmt.executeUpdate();
        } catch (Exception e) {
            m_Log.error(e.getMessage(), e);
            resultado = -1;
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
            return resultado;
        }
    }
    
    
   /**
     * Recupera el login de un usuario a partir de su código interno
     * @param codUsuario: Código del usuario
     * @param con: Conexión a la base de datos
     * @return String con el login o null sino se ha podido recuperar
     */
    public String getLoginUsuario(int codUsuario,Connection con){
        String login = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            String SQL = "SELECT USU_LOG FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU " +
                         "WHERE USU_COD=" + codUsuario;
            m_Log.debug(SQL);

            st = con.createStatement();
            rs = st.executeQuery(SQL);

            while(rs.next()){
                login = rs.getString("USU_LOG");
            }

        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return login;
    }

   public boolean tieneUnidadAncestroTipoRegistroAsignado(UORDTO uor,int codUnidadRegistro,String jndi){        
       boolean exito = false;
        
        
      if(uor.getUor_tipo()!=null && uor.getUor_tipo().equals("1") && Integer.toString(codUnidadRegistro).equals(uor.getUor_cod()))
          exito = true;          
      else{
          
          boolean salir = false;
          boolean encontrado = false;
          String codigo = null;
          
          codigo = uor.getUor_pad();
          
          while(!salir){
              
              if(codigo!=null && !"".equals(codigo)){
                UORDTO abuelo = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,codigo); 
                if(abuelo!=null){
                    if(abuelo.getUor_tipo()!=null && abuelo.getUor_tipo().equals("1") 
                            && abuelo.getUor_cod().equals(Integer.toString(codUnidadRegistro))){
                        salir= true;
                        encontrado = true;
                    }else
                        codigo = abuelo.getUor_pad();
                }else{
                    salir = true;
                    encontrado = false;
                }              
              }//if
              else{
                  salir = true;
                  encontrado = false;
              }
              
          }// while          
          
          if(salir){
              if(encontrado) exito = true;
              else exito = false;
          }
          
      }// else
              
      return exito;                    
        
   }
    
    
    public Integer getCodOficinaRegistro (int codUsuario, int codUnidadRegistro, Connection con, String jndi){
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodOficinaRegistro ( codUsuario = " + codUsuario + " codUnidadRegistro = " + codUnidadRegistro + " ) : BEGIN");
        Integer codOficinaRegistro = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            
            
            // Se recuperan las unidades sobre las que tiene permiso el usuario y que son oficinas de registro
            String sql = "SELECT UOU_UOR,UOR_COD_VIS,UOR_PAD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU,A_UOR " + 
                          "WHERE UOU_USU = " + codUsuario + " AND UOU_UOR=UOR_COD AND OFICINA_REGISTRO=1";
            m_Log.debug(sql) ;
            st = con.createStatement();
            rs = st.executeQuery(sql);
            ArrayList<UORDTO> oficinasUsuario = new ArrayList<UORDTO>();
            while(rs.next()){                
                UORDTO uor = new UORDTO();
                uor.setUor_cod(rs.getString("UOU_UOR"));
                uor.setUor_cod_vis(rs.getString("UOU_UOR"));
                uor.setUor_pad(rs.getString("UOR_PAD"));
                
                oficinasUsuario.add(uor);
            }
            
            st.close();
            rs.close();
                        
            // Para cada oficina de registro sobre la que el usuario tiene permiso, se busca aquella correspondiente a la
            // unidad de registro del parámetro "codUnidadRegistro"
            for(int i=0;i<oficinasUsuario.size();i++){
                UORDTO uor = oficinasUsuario.get(i);
                if(this.tieneUnidadAncestroTipoRegistroAsignado(uor, codUnidadRegistro, jndi)){
                    codOficinaRegistro = new Integer(uor.getUor_cod());
                    break;
                }
            }
           
            if(codOficinaRegistro==null){
                // Si no se ha encontrado la oficina de registro correspondiente => Entonces la unidad de registro hace de oficina
                codOficinaRegistro = new Integer(codUnidadRegistro);                
            }
            
            
        }catch (Exception e) {
            m_Log.error("Se ha producido un error recuperando las oficinas hijas de la unidad de registro", e);
        }finally {
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        
        
        return codOficinaRegistro;
    }//getCodOficinaRegistro
    
    /**
     * Recupera el número de oficinas de registro hijas de la unidad de registro en las que tenemos permiso
     * Lo correcto es tener permiso solo en una. Esta función se usa para comprobar en las JSPs si por error tenemos permiso en mas de una oficina
     * o no lo tenemos en ninguno.
     * 
     * En función de la unidad de registro busca las oficinas de registro hijas y comprueba si tenemos permiso en ellas.
     * Si no existen oficinas hijas el permiso se otorga directamente sobre la unidad de registro y esta sera la oficina de registro.
     * 
     * @param codUsuario código de usuario en Flexia
     * @param codUnidadRegistro unidad de registro seleccionada
     * @param con parametros de conexión a la BBDD
     * @return Integer con el número de oficinas
     */
    public Integer getNumOficinasRegistroPermiso (int codOrganizacion,int codUsuario, int codUnidadRegistro, Connection con){
        if(m_Log.isDebugEnabled()) m_Log.debug("getNumOficinasRegistroPermiso ( codUsuario = " + codUsuario + " codUnidadRegistro = " + codUnidadRegistro + " ) : BEGIN");
        Integer numOficinasRegistroPermiso = null;
        Statement st = null;
        ResultSet rs = null;
        //Buscamos para la codUnidadRegistro que oficinas de registro hijas disponibles hay.
        List<Integer> listaCodOficinasHijas = new ArrayList<Integer>();
        List<Integer> listaCodOficinasHijasPermisoUsuario = new ArrayList<Integer>();
        try{
            String sql = "Select UOR_COD from A_UOR where UOR_PAD = " + codUnidadRegistro + " and OFICINA_REGISTRO = 1";
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                listaCodOficinasHijas.add(Integer.valueOf(rs.getString("UOR_COD")));
            }//while(rs.next())
        }catch (Exception e) {
            m_Log.error("Se ha producido un error recuperando las oficinas hijas de la unidad de registro", e);
        }finally {
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        
        //Comprobamos si la oficina de registro tiene unidades hijas, si no tiene unidades hijas devolvemos el código de la propia unidad.
        if(listaCodOficinasHijas.size() > 0){
            if(m_Log.isDebugEnabled()) m_Log.debug("Recorremos el array de oficinas hijas buscando permisos");
            for(Integer uor : listaCodOficinasHijas){
                st = null;
                rs = null;
                try{                    
                    String sql = "Select UOU_UOR from " + GlobalNames.ESQUEMA_GENERICO + "a_uou auou where auou.uou_usu = " + codUsuario + " and auou.uou_uor = " + uor + " and auou.uou_org=" + codOrganizacion;
                    
                                        
                    if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        listaCodOficinasHijasPermisoUsuario.add(Integer.valueOf(rs.getString("UOU_UOR")));
                    }//while(rs.next())
                }catch (Exception e) {
                    m_Log.error("Se ha producido un error recuperando si el usuario tiene permisos en alguna unidad orgánica hija de la"
                            + " oficina de registro seleccionada ", e);
                }finally {
                    try{
                        if(m_Log.isDebugEnabled()) m_Log.debug("Procedemos a cerrar el statement y el resultset");
                        if(st!=null) st.close();
                        if(rs!=null) rs.close();
                    }catch(Exception e){
                        m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    }//try-catch
                }//try-catch-finally
                if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos el número de permisos que tiene el usuario en una oficina de registro");
                numOficinasRegistroPermiso = listaCodOficinasHijasPermisoUsuario.size();
            }//for(Integer uor : listaCodOficinasHijas)
        }else{
            if(m_Log.isDebugEnabled()) m_Log.debug("La unidad organizativa no tiene oficinas hijas. Devolvemos el código de la unidad de registro");
            numOficinasRegistroPermiso = 1;
        }//if(listaCodOficinasHijas.size() > 0)
        if(m_Log.isDebugEnabled()) m_Log.debug("número de oficinas de registro en las que tengo permiso = " + numOficinasRegistroPermiso);
        if(m_Log.isDebugEnabled()) m_Log.debug("getNumOficinasRegistroPermiso() : END");
        return numOficinasRegistroPermiso;
    }//getNumOficinasRegistroPermiso
    
    
    
    /**
     * Recupera las unidades de tipo Registro 
     * @param usuarioVO: Objeto de tipo UsuarioValueObject con los datos del usuario
     * @return ArrayList<UORDTO>: Coleccion con las uors 
     */ 
    public ArrayList<UORDTO> loadUnidadTipoRegistro(UsuarioValueObject usuarioVO,Connection con) throws TechnicalException {
        ArrayList<UORDTO> uors = new ArrayList<UORDTO>();       
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
                // Creamos la select con los parametros adecuados.
                String SQL_BUSCA_UNIDAD_ORG =
                                "SELECT "
                                        + uor_cod
                                        + ", "
                                        + uor_nom
                                        + ", "
                                        + uor_rex_general
                                        + " FROM A_UOR, " +GlobalNames.ESQUEMA_GENERICO+"A_UOU,"+GlobalNames.ESQUEMA_GENERICO+"A_ORG  WHERE "
                                        + uou_org
                                        + "	= ? AND "
                                        + uou_ent
                                        + "	= ? AND "
                                        + uou_usu
                                        + "	= ? AND "
                                        + uor_tip
                                        + "	= ? AND "
                                        + uou_uor
                                        + "	= "
                                        + uor_cod
                                        + "	AND "
                                        + uou_org
                                        + "	= "
                                        + org_cod ;

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_BUSCA_UNIDAD_ORG:	" + SQL_BUSCA_UNIDAD_ORG);

            stmt = con.prepareStatement(SQL_BUSCA_UNIDAD_ORG);
            stmt.setInt(1, usuarioVO.getOrgCod());
            stmt.setInt(2, usuarioVO.getEntCod());
            stmt.setInt(3, usuarioVO.getIdUsuario());
            stmt.setInt(4, 1);
        
            rs = stmt.executeQuery();
            while (rs.next()) {
                UORDTO uor = new UORDTO();
                uor.setUor_cod(rs.getString(uor_cod));
                uor.setUor_nom(rs.getString(uor_nom));                
                uor.setUor_rexistro_xeral(rs.getString(uor_rex_general));                
                uors.add(uor);
            }                
            
        } catch (Exception e) {
                m_Log.error(e.getMessage());
        } finally {            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
        
        return uors;
    }

    
    /**
     * Comprueba si un usuario tiene permiso sobre una determinada unidad organizativa
     * @param codOrganizacion: Código de la organización
     * @param codUsuario: Código del usuario
     * @param codUnidad: Código de la unidad
     * @param con: Conexión a BBDD
     * @return un boolean
     */
    public boolean tienePermisoUsuarioSobreUnidad(int codOrganizacion,int codUsuario,int codUnidad,Connection con){
        boolean exito = false;
        PreparedStatement ps  =null;
        ResultSet rs = null;
        
        String sql = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE UOU_USU=?" 
                   + " AND UOU_ORG=? AND UOU_UOR=?";        
        m_Log.debug(sql);
        try{
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,codUsuario);
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,codUnidad);
            
            rs = ps.executeQuery();
            int num = -1;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            
            if(num==1) exito = true;
            
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
            exito = false;
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Erro al cerrar recursos asociados a la conexión a BBDD: " + e.getMessage());
            }
        }        
        return exito;
    }
    
    
     /**
     * Comprobamos si existen dos logins iguales (uno en maiuscula y otro en minuscula)
     * @param login Login que queremos comprobar si hay dos iguales
     * @param con: Conexión a BBDD
     * @return  boolean
     */
    public boolean comprobarSiHayDos (String login) throws TechnicalException{
       
    m_Log.debug("comprobarSiHayDos.BEGIN: "+login);
    String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
    Statement st = null;
    ResultSet rs = null;
    AdaptadorSQLBD oad=null;
    Connection con=null;
     boolean resultado = false;
    
    try{    
        oad = new AdaptadorSQLBD(params);
        con = oad.getConnection();
        int num=-1; 
        String loginMaiuscula=login.toUpperCase();
        m_Log.debug("ComprobarSiHayDos.loginMaiuscula: "+ loginMaiuscula);
        String sql1 = "SELECT COUNT(*) as NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU  WHERE UPPER(USU_LOG)= '"+ loginMaiuscula+ "'" ;      
        m_Log.debug("ComprobarSiHayDos primera consulta: "+ sql1);
  
        st = con.createStatement();
        rs = st.executeQuery(sql1);
        if(rs.next()){
          num=rs.getInt("NUM");
        }
      
        if (num>1){
            resultado=true;
        } else
        {
            resultado=false;
        }    
       
       } catch (SQLException sqle) {
            throw new TechnicalException("ERROR DE ACCESO A BASE DE DATOS: " + sqle.getMessage(), sqle);
        } catch (BDException bde) {
            throw new TechnicalException("ERROR AL OBTENER EL ACCESO A BASE DE DATOS: " + bde.getMessage(), bde);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Erro al cerrar recursos asociados a la conexión a BBDD: " + e.getMessage());
            } 
    }   
        m_Log.debug("comprobarSiHayDos.END: "+resultado);
        return resultado;
    }
    
    
     /**
     * Comprobamos si existen el login que recibimos como parametro
     * @param login Login que queremos comprobar si hay dos iguales
     * @param con: Conexión a BBDD
     * @return  boolean true si existe, false en caso contrario
     */
    public boolean existeLogin(String login) throws TechnicalException{
       
        m_Log.debug("existeLogin.BEGIN: "+login);
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad=null;
        Connection con=null;
        boolean resultado = false;

        try{    
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            if(login!=null){   
                String sql = "SELECT USU_LOG  FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU  WHERE  USU_LOG = '"+login+ "'" ;      
                m_Log.debug("existeLogin.Consulta: "+ sql);

                st = con.createStatement();
                rs = st.executeQuery(sql);
                if(rs.next()){
                  resultado= true;
                }
            }else return false;

           } catch (SQLException sqle) {
                throw new TechnicalException("ERROR DE ACCESO A BASE DE DATOS: " + sqle.getMessage(), sqle);
            } catch (BDException bde) {
                throw new TechnicalException("ERROR AL OBTENER EL ACCESO A BASE DE DATOS: " + bde.getMessage(), bde);
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(con!=null) con.close();
                }catch(SQLException e){
                    m_Log.error("Erro al cerrar recursos asociados a la conexión a BBDD: " + e.getMessage());
                } 
        }   
        m_Log.debug("comprobarSiHayDos.END: "+resultado);
        return resultado;
    }
    

    
    
     /**
     * Comprueba si es necesario renovar la contraseña de un determinado usuario de Flexia
     * @param login: Login del usuario
     * @param con: Conexión a la BBDD
     * @return Un boolean
     */
    public boolean esNecesarioRenovarPasswordUsuario(String login){                
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = ""; 
        boolean salida = false;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        Connection con = null; 
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Se comprueba si existe el usuario, que no esté bloqueado ni dado de baja y que se le tenga que 
            // obligar, a que cambie su contraseña
            sql = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU " +                     
                  "WHERE USU_FBA IS NULL AND USU_CAMBIO_PASSWORD=1 AND USU_LOG='" + login + "' AND USU_BLQ=0";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            int num = 0;
            while(rs.next()) {
                num = rs.getInt("NUM");
            }
            
            if(num==1) salida = true;
            
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();                
                if(con!=null) con.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return salida;
    } 
    
    
    
   /**
     * Con este método se puede obligar a que un determinado usuario de Flexia, a que tenga que modificar su
     * password la siguiente vez que accede
     * @param codUsuario: Código del usuario
     * @param flag: true si se obliga a cambiar el password del usuario y false en caso contrario
     * @return Un boolean
     */
    public boolean cambiarCambioPasswordObligatorio(int codUsuario,boolean flag){                
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        boolean salida = false;
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD};
        Connection con = null; 
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Se comprueba si existe el usuario, que no esté bloqueado ni dado de baja y que se le tenga que 
            // obligar, a que cambie su contraseña
            int valor = 0;
            int i = 1;
            
            if(flag) valor = 1;             
            sql = "UPDATE A_USU SET USU_CAMBIO_PASSWORD=? WHERE USU_COD=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            stmt = con.prepareStatement(sql);
            stmt.setInt(i++,valor);
            stmt.setInt(i++,codUsuario);
            
            int rowsUpdated = stmt.executeUpdate();
            m_Log.debug("Número de filas actualizadas: " + rowsUpdated);
            if(rowsUpdated==1) salida = true;
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();                
                if(con!=null) con.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return salida;
    } 
    

    
    
    public String[] getNombreNifUsuario(int codUsuario, String[] params)
            throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String[] result = new String[2];
        String sql = "";

	try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + usu_nom + ", " + usu_nif
                + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE "
                + usu_cod + " ='" + codUsuario + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug("existeUsuarioCodigo: " + sql);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                result[0] = rs.getString(usu_nom);
                result[1] = rs.getString(usu_nif);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar nombre del usuario: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return result;
        }
    }      
    
    public ArrayList<GeneralValueObject> getListaUsuariosPorUOR(String codOrg, String codUOR, Connection con) throws SQLException{
        ArrayList<GeneralValueObject> lista = null;
        String query;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            query = "SELECT A_UOR.UOR_COD, A_UOR.UOR_NOM || ' (' || A_UOR.UOR_COD_VIS || ')' AS NOMBRE, A_USU.USU_NOM || '(' || A_USU.USU_LOG || ')' AS USU "
                    + "FROM A_UOR INNER JOIN " + GlobalNames.ESQUEMA_GENERICO+ "A_UOU A_UOU ON A_UOR.UOR_COD=A_UOU.UOU_UOR "
                    + "INNER JOIN " + GlobalNames.ESQUEMA_GENERICO+ "A_USU A_USU ON A_UOU.UOU_USU = A_USU.USU_COD "
                    + "WHERE A_UOU.UOU_ORG=?";
            
            if(!codUOR.equals("TODAS")){
                query += " AND A_UOR.UOR_COD=?" ;
            } 
            query += " ORDER BY UOR_COD,USU";
            m_Log.debug("consulta: "+query);
            
            int contbd = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contbd++, Integer.parseInt(codOrg));
            if(!codUOR.equals("TODAS")){
                ps.setInt(contbd++, Integer.parseInt(codUOR));
            }
            rs = ps.executeQuery();
            
            lista = new ArrayList<GeneralValueObject>();
            int codUnidad = -1;
            String unidad = null;
            ArrayList<String> listaUsuarios = new ArrayList<String>();
            GeneralValueObject gVO = new GeneralValueObject();
            while(rs.next()){
                if(codUnidad==-1 || codUnidad!=rs.getInt("UOR_COD")){
                    codUnidad = rs.getInt("UOR_COD");
                    unidad = rs.getString("NOMBRE");
                    if(listaUsuarios.size()>0){
                        gVO.setAtributo("usuarios", listaUsuarios);
                        lista.add(gVO);
                        listaUsuarios = new ArrayList<String>();
                        gVO = new GeneralValueObject();
                    }
                    gVO.setAtributo("elemento", unidad);
                }
                listaUsuarios.add(rs.getString("USU"));
            }
            gVO.setAtributo("elemento", unidad);
            gVO.setAtributo("usuarios", listaUsuarios);
            lista.add(gVO);
            
        } catch (Exception ex){
            m_Log.error("Ha ocurrido un error al obtener los usuarios por unidad orgánica");
            throw new SQLException(ex.getMessage());
        } finally {
            try  {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception e){
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        return lista;
    }
    
    public ArrayList<GeneralValueObject> getListaUsuariosPorProcedimiento(String codProc, Connection con) throws SQLException{
        ArrayList<GeneralValueObject> lista = null;
        String query;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            query = "SELECT PRO_COD||' - '||PML_VALOR AS PROC, (CASE WHEN NVL(USU_NOM,'x')='x' THEN 'CUALQUIERA' ELSE USU_NOM ||'('|| USU_LOG||')' END) AS USU "
                    + "FROM E_PRO INNER JOIN E_PML ON (E_PRO.PRO_MUN=E_PML.PML_MUN AND E_PRO.PRO_COD=E_PML.PML_COD) "
                    + "LEFT JOIN E_PUI ON E_PRO.PRO_COD = E_PUI.PUI_PRO LEFT JOIN A_UOR ON E_PUI.PUI_COD=A_UOR.UOR_COD "
                    + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO+ "A_UOU A_UOU ON (A_UOR.UOR_COD=A_UOU.UOU_UOR AND A_UOU.UOU_ORG=E_PRO.PRO_MUN) "
                    + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO+ "A_USU A_USU ON A_UOU.UOU_USU = A_USU.USU_COD";
            
            if(!codProc.equals("TODOS")){
                query += " WHERE PRO_COD=?" ;
            } 
            query += " GROUP BY PRO_COD, PML_VALOR, USU_NOM, USU_LOG ORDER BY PRO_COD,USU";
            m_Log.debug("consulta: "+query);
            
            ps = con.prepareStatement(query);
            if(!codProc.equals("TODOS")){
                ps.setString(1, codProc);
            }
            rs = ps.executeQuery();
            
            lista = new ArrayList<GeneralValueObject>();
            String proced = null;
            ArrayList<String> listaUsuarios = new ArrayList<String>();
            GeneralValueObject gVO = new GeneralValueObject();
            while(rs.next()){
                if(proced==null || !proced.equals(rs.getString("PROC"))){
                    proced = rs.getString("PROC");
                    if(listaUsuarios.size()>0){
                        gVO.setAtributo("usuarios", listaUsuarios);
                        lista.add(gVO);
                        listaUsuarios = new ArrayList<String>();
                        gVO = new GeneralValueObject();
                    }
                    gVO.setAtributo("elemento", proced);
                }
                listaUsuarios.add(rs.getString("USU"));
            }
            gVO.setAtributo("elemento", proced);
            gVO.setAtributo("usuarios", listaUsuarios);
            lista.add(gVO);
            
        } catch (Exception ex){
            m_Log.error("Ha ocurrido un error al obtener los datos de los usuarios tramitadores de cada procedimiento");
            throw new SQLException(ex.getMessage());
        } finally {
            try  {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception e){
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        return lista;
    }
    
    
}//class
