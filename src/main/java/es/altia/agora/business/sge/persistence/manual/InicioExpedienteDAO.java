package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.InicioExpedienteValueObject;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.Vector;

public class InicioExpedienteDAO {

	//Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
	//Para los mensajes de error localizados.
    protected static Config m_ConfigError;
	//Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(InicioExpedienteDAO.class.getName());

	protected static String exr_mun;
    protected static String exr_pro;
    protected static String exr_eje;
	protected static String exr_num;
	protected static String exr_der;
	protected static String exr_uor;
	protected static String exr_ejr;
	protected static String exr_nur;
	protected static String exr_tip;
	protected static String exr_ori;
	protected static String exr_top;

	protected static String exp_mun;
    protected static String exp_pro;
    protected static String exp_eje;
	protected static String exp_num;
	protected static String exp_fei;
	protected static String exp_fef;
	protected static String exp_ter;
	protected static String exp_tnv;
	protected static String exp_est;

	protected static String res_dep;
	protected static String res_est;
    protected static String res_uor;
    protected static String res_eje;
	protected static String res_num;
	protected static String res_tip;

	protected static String pro_cod;
    protected static String pro_nom;
    protected static String pro_mun;

	protected static String nex_mun;
	protected static String nex_pro;
	protected static String nex_eje;
	protected static String nex_num;

    private static InicioExpedienteDAO instance = null;	

	protected InicioExpedienteDAO() {
		super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

		exr_mun = m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
		exr_pro = m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
		exr_eje = m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
		exr_num = m_ConfigTechnical.getString("SQL.E_EXR.numero");
		exr_der = m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
		exr_uor = m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
		exr_ejr = m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
		exr_nur = m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
		exr_tip = m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
		exr_ori = m_ConfigTechnical.getString("SQL.E_EXR.origen");
		exr_top = m_ConfigTechnical.getString("SQL.E_EXR.tipoOperacion");

		exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
		exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
		exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
		exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
		exp_fei = m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
		exp_fef = m_ConfigTechnical.getString("SQL.E_EXP.fechaFin");
		exp_ter = m_ConfigTechnical.getString("SQL.E_EXP.tercero");
		exp_tnv = m_ConfigTechnical.getString("SQL.E_EXP.version");
		exp_est = m_ConfigTechnical.getString("SQL.E_EXP.estado");
		
		res_dep = m_ConfigTechnical.getString("SQL.R_RES.codDpto");
		res_est = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
		res_uor = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
		res_eje = m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
		res_num = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
		res_tip = m_ConfigTechnical.getString("SQL.R_RES.tipoReg");

		pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
		pro_nom = m_ConfigTechnical.getString("SQL.E_PRO.nombreProc");
		pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");

		nex_mun = m_ConfigTechnical.getString("SQL.E_NEX.codMunicipio");
		nex_pro = m_ConfigTechnical.getString("SQL.E_NEX.codProcedimiento");
		nex_eje = m_ConfigTechnical.getString("SQL.E_NEX.ejercicio");
		nex_num = m_ConfigTechnical.getString("SQL.E_NEX.numero");

	}

	public static InicioExpedienteDAO getInstance() {
		//si no hay ninguna instancia de esta clase tenemos que crear una.
		if (instance == null) {
			// Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
			synchronized(InicioExpedienteDAO.class){
				if (instance == null)
					instance = new InicioExpedienteDAO();
			}
		}
		return instance;
	}	

	public int insertExpediente(InicioExpedienteValueObject inicioVO, String[] params) throws TechnicalException {
		m_Log.info("insertExpediente()::BEGIN");
		AdaptadorSQLBD oad = null;
		Connection con = null;
		Statement st = null;	
		ResultSet rs = null;
		String sql = "";
		int res = -1;			

		try{			
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
			oad.inicioTransaccion(con) ; // Inicio transacción
			st = con.createStatement();			
			
			// Creamos la select con los parametros adecuados.

			String ano = "";
			String num = "";
			
			if(inicioVO.getEjercicio() != null) ano = inicioVO.getEjercicio();
			if(inicioVO.getNumero() != null) num = inicioVO.getNumero();

			if(m_Log.isDebugEnabled()) m_Log.debug("INICIO: "+inicioVO.getCodMunicipio());
			int ideMax = 0;
			sql = "select " + oad.funcionSistema(oad.FUNCIONSISTEMA_NVL,
                new String[]{oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{nex_num}), "0"}) +
            " from e_nex where " + nex_mun + " = " + Integer.parseInt(inicioVO.getCodMunicipio()) + " and " + nex_pro + " = '" + inicioVO.getCodProcedimiento() + "' and " + nex_eje + " = " + oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "YYYY");
			if(m_Log.isInfoEnabled()) m_Log.info("Query para consultar el número de expediente siguiente = " + sql);
			rs = st.executeQuery(sql);
			while(rs.next()){
				ideMax = rs.getInt(oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{nex_num}));
			}
			rs.close();

			num = String.valueOf(ideMax+1);
			ano = oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "YYYY");

			if(ideMax == 0){
				sql = "insert into e_nex (" + nex_mun + ", " + nex_pro + ", " + nex_eje + ", " + nex_num + ") values (" + Integer.parseInt(inicioVO.getCodMunicipio()) + ", '" + inicioVO.getCodProcedimiento() + "', " + ano + ", " + num + ")";
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				res = st.executeUpdate(sql);
			}else{
				sql = "update e_nex set " + nex_num + " = " + num + " where " + nex_mun + " = " + Integer.parseInt(inicioVO.getCodMunicipio()) + " and " + nex_pro + " = '" + inicioVO.getCodProcedimiento() + "' and " + nex_eje + " = " + ano;
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				res = st.executeUpdate(sql);
			}
			m_Log.info("se actualiza/inserta en E_NEX el último número de expediente: " + num);

			if("false".equals(inicioVO.getIniciado())){ 
                            // #253742: Añadir valor de fecha actual al insertar
                            java.util.Date fechoraActual = new java.util.Date();
                            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(fechoraActual.getTime());  
				sql = "insert into e_exr (" + exr_mun + ", " + exr_pro + ", " + exr_eje + ", " + exr_num + ", " + exr_der + ", " + exr_uor + ", " + exr_ejr + ", " + exr_nur + ", " + exr_tip + ", " + exr_ori + ", " + exr_top + ",EXR_FECHAINSMOD) "
                                        + "values (" + Integer.parseInt(inicioVO.getCodMunicipio()) + ", '" + inicioVO.getCodProcedimiento() + "', " + ano + ", " + num + ", " + Integer.parseInt(inicioVO.getDepartamentoRes()) + ", " + Integer.parseInt(inicioVO.getUnidadOrgRes()) + ", " + Integer.parseInt(inicioVO.getEjercicioRes())
                                        + ", " + Integer.parseInt(inicioVO.getNumeroRes()) + ", '" + inicioVO.getTipoRes() + "', 1, 'I'," + oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ")";
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				res = st.executeUpdate(sql);				
			}
			
      sql = "insert into e_exp (" + exp_mun + ", " + exp_pro + ", " + exp_eje + ", " + exp_num + ", " + exp_fei + ", " + exp_fef + ", " + exp_ter + ", " + exp_tnv + ", " + exp_est + ") values (" + Integer.parseInt(inicioVO.getCodMunicipio()) + ", '" + inicioVO.getCodProcedimiento() + "', " + ano + ", " 
        + num + ", " + oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", null, " + Integer.parseInt(inicioVO.getTercero()) + ", " + Integer.parseInt(inicioVO.getVersion()) + ", 1)";
			if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			res = st.executeUpdate(sql);					

			sql = "update r_res set " + res_est + " = " + Integer.parseInt(inicioVO.getEstado()) + " where " +  res_dep + " = " +  Integer.parseInt(inicioVO.getDepartamentoRes()) + " and " + res_uor + " = " + Integer.parseInt(inicioVO.getUnidadOrgRes()) + " and " + res_eje + " = " + Integer.parseInt(inicioVO.getEjercicioRes()) + " and " + res_num + " = " + Integer.parseInt(inicioVO.getNumeroRes()) + " and " + res_tip + " = '" + inicioVO.getTipoRes() + "'";
			if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			res = st.executeUpdate(sql);

            oad.finTransaccion(con);

        }catch (Exception e){
			try {
				oad.rollBack(con);
			} catch(BDException bde) {
                bde.printStackTrace();
                bde.getMensaje();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
			e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                if (st!=null) st.close();
                oad.devolverConexion(con);
            } catch(BDException bde) {
                bde.getMensaje();
            } catch(SQLException sqle){
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
			return res;
		}
	}
	
	public Vector loadProcedimientos(UsuarioValueObject usuarioVO) throws TechnicalException {	
		AdaptadorSQLBD oad = null;		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Vector procedimientos = new Vector();		

		try{			
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());  
			con = oad.getConnection();		
			st = con.createStatement();

			// Creamos la select con los parametros adecuados.		

			String SQL_LOAD_PROCED = "SELECT " + pro_cod + ", " + pro_nom + " FROM e_pro WHERE " + pro_mun + " = " + usuarioVO.getOrgCod();
			
			String[] parametros = new String[3];
			parametros[0] = pro_nom;
			parametros[1] = "2";
			SQL_LOAD_PROCED = SQL_LOAD_PROCED + oad.orderUnion(parametros);

			if(m_Log.isDebugEnabled()) m_Log.debug(SQL_LOAD_PROCED);

			rs = st.executeQuery(SQL_LOAD_PROCED);					
			while(rs.next()){			
				String codProcedimiento = rs.getString(pro_cod);
				String nomProcedimiento = rs.getString(pro_nom);
				InicioExpedienteValueObject incioVO = new InicioExpedienteValueObject(codProcedimiento, nomProcedimiento);
				procedimientos.addElement(incioVO);				
			}
			
		}catch (Exception e){
			e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{				
                if (rs!=null) rs.close();
                if (st!=null) st.close();
				oad.devolverConexion(con);
			}catch(Exception bde) {
				bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
			return procedimientos;
		}	
	}
}
