package es.altia.agora.business.util.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class MenuDAO {

	//Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
	//Para los mensajes de error localizados.
    protected static Config m_ConfigError;

    protected static Log m_Log =
            LogFactory.getLog(MenuDAO.class.getName());


    private static MenuDAO instance = null;

	protected static String mor_ele;
	protected static String mor_apl;
	protected static String mor_pro;
	protected static String mor_org;
	protected static String mor_pad;
	protected static String mor_mnu;

	protected static String pid_apl;
	protected static String pid_pro;
	protected static String pid_idi;
	protected static String pid_des;

	protected static String pro_apl;
	protected static String pro_cod;
	protected static String pro_frm;

	protected static String mnu_cod;
	protected static String mnu_org;
	protected static String mnu_apl;

	protected static String ugo_gru;
	protected static String ugo_usu;
	protected static String ugo_org;
	protected static String ugo_ent;
	protected static String ugo_apl;


	protected static String rpg_gru;
	protected static String rpg_apl;
	protected static String rpg_org;
	protected static String rpg_ent;
	protected static String rpg_tip;
	protected static String rpg_pro;

	protected static String rpu_usu;
	protected static String rpu_apl;
	protected static String rpu_org;
	protected static String rpu_ent;
	protected static String rpu_tip;
	protected static String rpu_pro;

	protected MenuDAO() {
	  //Queremos usar el fichero de configuracion techserver
	  m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	  //Queremos tener acceso a los mensajes de error localizados
	  m_ConfigError = ConfigServiceHelper.getConfig("error");

		mor_ele = m_ConfigTechnical.getString("SQL.A_MOR.elemento");
		mor_apl = m_ConfigTechnical.getString("SQL.A_MOR.aplicacion");
		mor_pro = m_ConfigTechnical.getString("SQL.A_MOR.proceso");
		mor_org = m_ConfigTechnical.getString("SQL.A_MOR.organizacion");
		mor_pad = m_ConfigTechnical.getString("SQL.A_MOR.padre");
		mor_mnu = m_ConfigTechnical.getString("SQL.A_MOR.menu");

		pid_apl = m_ConfigTechnical.getString("SQL.A_PID.aplicacion");
		pid_pro = m_ConfigTechnical.getString("SQL.A_PID.proceso");
		pid_idi = m_ConfigTechnical.getString("SQL.A_PID.idioma");
		pid_des = m_ConfigTechnical.getString("SQL.A_PID.descripcion");

		pro_apl = m_ConfigTechnical.getString("SQL.A_PRO.aplicacion");
		pro_cod = m_ConfigTechnical.getString("SQL.A_PRO.codigo");
		pro_frm = m_ConfigTechnical.getString("SQL.A_PRO.formulario");

		mnu_cod = m_ConfigTechnical.getString("SQL.A_MNU.codigo");
		mnu_org = m_ConfigTechnical.getString("SQL.A_MNU.organizacion");
		mnu_apl = m_ConfigTechnical.getString("SQL.A_MNU.aplicacion");

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

		rpu_org = m_ConfigTechnical.getString("SQL.A_RPU.organizacion");
		rpu_ent = m_ConfigTechnical.getString("SQL.A_RPU.entidad");
		rpu_apl = m_ConfigTechnical.getString("SQL.A_RPU.aplicacion");
		rpu_tip = m_ConfigTechnical.getString("SQL.A_RPU.tipo");
		rpu_usu = m_ConfigTechnical.getString("SQL.A_RPU.usuario");
		rpu_pro = m_ConfigTechnical.getString("SQL.A_RPU.proceso");

	}

	public static MenuDAO getInstance() {
	  //si no hay ninguna instancia de esta clase tenemos que crear una.
	  synchronized(MenuDAO.class){
		    if (instance == null)
			  instance = new MenuDAO();
		    }
	  return instance;
	}

	public Vector loadMenu(UsuarioValueObject usuarioVO) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;		

		Vector menu = new Vector();

		try{		
			
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());
			con = oad.getConnection();	

			// Creamos la select con los parametros adecuados.
			String sentencia = "SELECT pad, ele, des, frm FROM (" +
											"SELECT " + mor_pad + " AS pad, " + mor_ele + " AS ele, " + pid_des + " AS des, " + pro_frm + " AS frm " +
                                            "FROM "+ GlobalNames.ESQUEMA_GENERICO+"a_ugo a_ugo,"+GlobalNames.ESQUEMA_GENERICO+"a_rpg a_rpg,"+GlobalNames.ESQUEMA_GENERICO+"a_pro a_pro,"+
                                                     GlobalNames.ESQUEMA_GENERICO+"a_mor a_mor,"+GlobalNames.ESQUEMA_GENERICO + "a_pid a_pid,"+GlobalNames.ESQUEMA_GENERICO + "a_mnu a_mnu WHERE " +
													ugo_gru + " = " + rpg_gru + " AND " +
													ugo_apl + " = " + rpg_apl + " AND " +
													ugo_org + " = " + rpg_org + " AND " +
													ugo_ent + " = " + rpg_ent + " AND " +
		
													rpg_apl + " = " + pro_apl + " AND " +
													rpg_pro + " = " + pro_cod + " AND " +
			
													pro_apl + " = " + pid_apl + " AND " +
													pro_cod + " = " + pid_pro + " AND " +
			
													mor_org + " = " + rpg_org + " AND " +
													mor_pro + " = " + rpg_pro + " AND " +
													mor_apl + " = " + rpg_apl + " AND " +
		
													mnu_org + " = " + mor_org + " AND " +
													mnu_apl + " = " + mor_apl + " AND " +
													mnu_cod + " = " + mor_mnu + " AND " +	
			
													ugo_usu + " = ? AND " +
													ugo_apl + " = ? AND " +
													ugo_org + " = ? AND " +
													ugo_ent + " = ? AND " +
													pid_idi + " = ? AND " + 	
													rpg_tip + " = 1 " +
											"UNION SELECT " + mor_pad + " AS pad, " + mor_ele + " AS ele, " + pid_des + " AS des, " + pro_frm + " AS frm " +
                                            "FROM "+ GlobalNames.ESQUEMA_GENERICO+"a_ugo a_ugo,"+GlobalNames.ESQUEMA_GENERICO+"a_rpu a_rpu,"+GlobalNames.ESQUEMA_GENERICO+"a_pro a_pro,"+
                                                     GlobalNames.ESQUEMA_GENERICO+"a_mor a_mor,"+GlobalNames.ESQUEMA_GENERICO + "a_pid a_pid,"+GlobalNames.ESQUEMA_GENERICO + "a_mnu a_mnu WHERE " +
                                                    ugo_usu + " = " + rpu_usu + " AND " +
													ugo_apl + " = " + rpu_apl + " AND " +
													ugo_org + " = " + rpu_org + " AND " +
													ugo_ent + " = " + rpu_ent + " AND " +
	
													rpu_apl + " = " + pro_apl + " AND " +
													rpu_pro + " = " + pro_cod + " AND " +
			
													pro_apl + " = " + pid_apl + " AND " +
													pro_cod + " = " + pid_pro + " AND " +
			
													mor_org + " = " + rpu_org + " AND " +
													mor_pro + " = " + rpu_pro + " AND " +
													mor_apl + " = " + rpu_apl + " AND " +
		
													mnu_org + " = " + mor_org + " AND " +
													mnu_apl + " = " + mor_apl + " AND " +
													mnu_cod + " = " + mor_mnu + " AND " +	
	
													ugo_usu + " = ? AND " +
													ugo_apl + " = ? AND " +
													ugo_org + " = ? AND " +
													ugo_ent + " = ? AND " +
													pid_idi + " = ? AND " +	
													rpu_tip + " = 1 " +
									") s ";
            String condicionSelect = "SELECT " + mor_pad + " AS pad, " + mor_ele + " AS ele, " + pid_des + " AS des, " + pro_frm + " AS frm " +
                                     "FROM "+ GlobalNames.ESQUEMA_GENERICO+"a_ugo a_ugo,"+GlobalNames.ESQUEMA_GENERICO+"a_rpu a_rpu,"+GlobalNames.ESQUEMA_GENERICO+"a_pro a_pro,"+
                                              GlobalNames.ESQUEMA_GENERICO+"a_mor a_mor,"+GlobalNames.ESQUEMA_GENERICO + "a_pid a_pid,"+GlobalNames.ESQUEMA_GENERICO + "a_mnu a_mnu WHERE " +
                                            ugo_usu + " = " + rpu_usu + " AND " +
											ugo_apl + " = " + rpu_apl + " AND " +
											ugo_org + " = " + rpu_org + " AND " +
											ugo_ent + " = " + rpu_ent + " AND " +
	
											rpu_apl + " = " + pro_apl + " AND " +
											rpu_pro + " = " + pro_cod + " AND " +
	
											pro_apl + " = " + pid_apl + " AND " +
											pro_cod + " = " + pid_pro + " AND " +
	
											mor_org + " = " + rpu_org + " AND " +
											mor_pro + " = " + rpu_pro + " AND " +
											mor_apl + " = " + rpu_apl + " AND " +
	
											mnu_org + " = " + mor_org + " AND " +
											mnu_apl + " = " + mor_apl + " AND " +
											mnu_cod + " = " + mor_mnu + " AND " +	
	
											ugo_usu + " = ? AND " +
											ugo_apl + " = ? AND " +
											ugo_org + " = ? AND " +
											ugo_ent + " = ? AND " +
											pid_idi + " = ? AND " +	
											rpu_tip + " = 0 ";
			
            String SQL_BUSCA_MENU = "";
            if(usuarioVO.getParamsCon()[0]!=null && ("SQLSERVER".equalsIgnoreCase(usuarioVO.getParamsCon()[0]))){
                SQL_BUSCA_MENU = sentencia + " EXCEPT " + condicionSelect;
            } else {
                SQL_BUSCA_MENU = sentencia + oad.minus(sentencia, condicionSelect);
            }

            stmt = con.prepareStatement(SQL_BUSCA_MENU);
            m_Log.debug("SQL_BUSCA_MENU "+SQL_BUSCA_MENU);
			stmt.setInt(1, usuarioVO.getIdUsuario());
			stmt.setInt(2, usuarioVO.getAppCod());
			stmt.setInt(3, usuarioVO.getOrgCod());
			stmt.setInt(4, usuarioVO.getEntCod());
			stmt.setInt(5, usuarioVO.getIdioma());
			
			stmt.setInt(6, usuarioVO.getIdUsuario());
			stmt.setInt(7, usuarioVO.getAppCod());
			stmt.setInt(8, usuarioVO.getOrgCod());
			stmt.setInt(9, usuarioVO.getEntCod());
			stmt.setInt(10, usuarioVO.getIdioma());
		
			stmt.setInt(11, usuarioVO.getIdUsuario());
			stmt.setInt(12, usuarioVO.getAppCod());
			stmt.setInt(13, usuarioVO.getOrgCod());
			stmt.setInt(14, usuarioVO.getEntCod());
			stmt.setInt(15, usuarioVO.getIdioma());
			
			rs = stmt.executeQuery();	

			while(rs.next()){
				menu.addElement(rs.getString("pad"));
				menu.addElement(rs.getString("ele"));
				menu.addElement(rs.getString("des"));
				menu.addElement(rs.getString("frm"));
			}	
			rs.close();
			stmt.close();
            return menu;
        }catch (Exception e){            
            m_Log.error(e.getMessage());
            return null;
        }finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{				
				oad.devolverConexion(con);
			}catch(BDException bde) {
				bde.getMensaje();
			}
		}
	}

}