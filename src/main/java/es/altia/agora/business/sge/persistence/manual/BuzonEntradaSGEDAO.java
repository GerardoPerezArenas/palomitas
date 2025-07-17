package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.BuzonEntradaSGEValueObject;
import es.altia.agora.business.sge.InicioExpedienteValueObject;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.Iterator;
import java.util.Vector;

public class BuzonEntradaSGEDAO {

	//Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
	//Para los mensajes de error localizados.
    protected static Config m_ConfigError;
	//Para informacion de logs.
    protected static Log m_Log =
             LogFactory.getLog(BuzonEntradaSGEDAO.class.getName());

	protected static String pro_nom;
	protected static String pro_mun;
	protected static String pro_cod;

	protected static String hte_nan;
	protected static String hte_a1a;
	protected static String hte_a2a;
	protected static String hte_ter;
	protected static String hte_nvr;

	protected static String exr_pro;
	protected static String exr_eje;
	protected static String exr_num;
	protected static String exr_mun;
	protected static String exr_der;
	protected static String exr_uor;
	protected static String exr_ejr;
	protected static String exr_nur;
	protected static String exr_tip;
	protected static String exr_top;
	
	protected static String res_ter;
    protected static String res_tnv;
    protected static String res_dom;
	protected static String res_fec;
	protected static String res_dep;
	protected static String res_uor;
	protected static String res_eje;
	protected static String res_tip;
	protected static String res_num;
	//protected static String res_dod;
	protected static String res_uod;
	protected static String res_est;

    private static BuzonEntradaSGEDAO instance = null;	

	protected BuzonEntradaSGEDAO() {
		super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

		pro_nom = m_ConfigTechnical.getString("SQL.E_PRO.nombreProc");
		pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
		pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");

		hte_nan = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
		hte_a1a = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
		hte_a2a = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
		hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
		hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");

		exr_pro = m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
		exr_eje = m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
		exr_num = m_ConfigTechnical.getString("SQL.E_EXR.numero");
		exr_mun = m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
		exr_der = m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
		exr_uor = m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
		exr_ejr = m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
		exr_nur = m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
		exr_tip = m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
		exr_top = m_ConfigTechnical.getString("SQL.E_EXR.tipoOperacion");
		
		res_ter = m_ConfigTechnical.getString("SQL.R_RES.codTercero");
		res_tnv = m_ConfigTechnical.getString("SQL.R_RES.modifInteresado");
		res_dom = m_ConfigTechnical.getString("SQL.R_RES.domicTercero");
		res_fec = m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");
		res_dep = m_ConfigTechnical.getString("SQL.R_RES.codDpto");
		res_uor = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
		res_eje = m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
		res_tip = m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
		res_num = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
		//res_dod = m_ConfigTechnical.getString("SQL.R_RES.departOrigDest");
		res_uod = m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");
		res_est = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
	}

	public static BuzonEntradaSGEDAO getInstance() {
		//si no hay ninguna instancia de esta clase tenemos que crear una.
		if (instance == null) {
			// Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
			synchronized(BuzonEntradaSGEDAO.class){
				if (instance == null)
					instance = new BuzonEntradaSGEDAO();
			}
		}
		return instance;
	}	

	public Vector loadIniciados(UsuarioValueObject usuarioVO) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Vector iniciados = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try{
			oad = new AdaptadorSQLBD(usuarioVO.getParamsCon());
			con = oad.getConnection();		

			// Creamos la select con los parametros adecuados.
			String[] joins = new String[11];
			joins[0] = "e_exr";
			joins[1] = "LEFT";
			joins[2] = "r_res";
			joins[3] = res_dep + " = " + exr_der + " AND " + res_uor + " = " + exr_uor  + " AND " + res_eje + " = " + exr_ejr + " AND " + res_tip + " = " + exr_tip + " AND " + res_num + " = " + exr_nur;
			joins[4] = "INNER";
			joins[5] = "t_hte";
			joins[6] = res_ter + " = " + hte_ter + " AND " + res_tnv + " = " + hte_nvr;	
			joins[7] = "LEFT";
			joins[8] = "e_pro";
			joins[9] = exr_mun + " = " + pro_mun + " AND " + exr_pro + " = " + pro_cod;	
			joins[10] = "false";						


			String PARTE_SELECT = oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{
                    oad.convertir(res_eje, oad.CONVERTIR_COLUMNA_TEXTO, null), "'/'", oad.convertir(res_num,
                    oad.CONVERTIR_COLUMNA_TEXTO, null)}) +
                    " AS registro, " + pro_nom + ", " + exr_pro + ", " +
                    exr_mun + ", " + exr_eje + ", " + exr_num + ", " + res_dep + ", " + res_uor + ", " +
                    res_eje + ", " + res_num + ", " + res_tip + ",  " +
                    oad.convertir(res_fec, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fecha_inicio, " +
                    res_ter + ", " + res_tnv + ", " + res_dom + ", " + hte_nan + ", " + hte_a1a + ", " + hte_a2a;
            // tiruritata
            String PARTE_WHERE = res_est + " = 1 AND " /*+ res_dod + " = ? AND "*/ + res_uod + " = ? ";
			String SQL_BUSCA_INICIADOS = oad.join(PARTE_SELECT, PARTE_WHERE, joins);

			String[] parametros = new String[3];
			parametros[0] = "registro";
			parametros[1] = "1";
			SQL_BUSCA_INICIADOS = SQL_BUSCA_INICIADOS + oad.orderUnion(parametros);

			if(m_Log.isDebugEnabled())
                m_Log.debug("SQL_BUSCA_INICIADOS: " + SQL_BUSCA_INICIADOS);
			
			stmt = con.prepareStatement(SQL_BUSCA_INICIADOS);
            // tiruritata
            //stmt.setInt(1,usuarioVO.getDepCod());
			// esto lo comento pq sólo hay un param
            //stmt.setInt(2,usuarioVO.getUnidadOrgCod());
            stmt.setInt(1,usuarioVO.getUnidadOrgCod());
            rs = stmt.executeQuery();
			while(rs.next()){				
				String solicitante = rs.getString(hte_nan);
				String nomProcedimiento = "";
				String codMunicipio = "";
				String codProcedimiento = "";
				String ejercicio = "";
				String numero = "";

				if((rs.getString(hte_a1a) != null)&&(rs.getString(hte_a2a) != null)){
					solicitante = rs.getString(hte_a1a) + " " + rs.getString(hte_a2a) + ", " + solicitante;
				}				
				if(rs.getString(pro_nom) != null) nomProcedimiento = rs.getString(pro_nom);
				if(rs.getString(exr_mun) != null) codMunicipio = rs.getString(exr_mun);
				if(rs.getString(exr_pro) != null) codProcedimiento = rs.getString(exr_pro);
				if(rs.getString(exr_eje) != null) ejercicio = rs.getString(exr_eje);
				if(rs.getString(exr_num) != null) numero = rs.getString(exr_num);				

				BuzonEntradaSGEValueObject buzonVO = new BuzonEntradaSGEValueObject(rs.getString("registro"), nomProcedimiento, codMunicipio, codProcedimiento, ejercicio, numero, rs.getString(res_dep), rs.getString(res_uor), rs.getString(res_eje), rs.getString(res_num), rs.getString(res_tip), rs.getString("fecha_inicio"), rs.getString(res_ter), rs.getString(res_tnv), rs.getString(res_dom), solicitante);
				iniciados.addElement(buzonVO);				
			}
			
			rs.close();
			stmt.close();
		}catch (Exception e){
			m_Log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{
				oad.devolverConexion(con);
			}catch(BDException bde) {
				bde.getMensaje();
			}
			return iniciados;
		}
	}	

	public int modifyEstado(Vector inicioVector, String[] params) throws TechnicalException {
		AdaptadorSQLBD oad = null;
		Connection con = null;
		Statement st = null;			
		int res = -1;	
		

		try{			
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
			st = con.createStatement();						
			
			Iterator iter = inicioVector.iterator();
			InicioExpedienteValueObject inicioVO = null;
			while (iter.hasNext()){
				inicioVO = (InicioExpedienteValueObject)iter.next();				

				// Creamos la select con los parametros adecuados.
				String SQL_MODIFY_ESTADO = "update r_res set " + res_est + " = " + Integer.parseInt(inicioVO.getEstado()) + " where " +  res_dep + " = " + Integer.parseInt(inicioVO.getDepartamentoRes()) + " and " + res_uor + " = " + Integer.parseInt(inicioVO.getUnidadOrgRes()) + " and " + res_eje + " = " + Integer.parseInt(inicioVO.getEjercicioRes()) + " and " + res_num + " = " + Integer.parseInt(inicioVO.getNumeroRes()) + " and " + res_tip + " = '" + inicioVO.getTipoRes() + "'";
				if(m_Log.isDebugEnabled()) m_Log.debug(SQL_MODIFY_ESTADO);	
				res += st.executeUpdate(SQL_MODIFY_ESTADO);
			}
			
			st.close();
		}catch (Exception e){
			m_Log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{				
				oad.devolverConexion(con);
			}catch(BDException bde) {
				bde.getMensaje();
			}
			return res;
		}
	}

}