/**
 * UtilesXerador.java
 *
 * @author Julio J. Gómez Díaz. Altia Consultores <julio.gomez@altia.es>
 */

package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;
import es.altia.util.HashtableWithNull;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Types;
import java.util.Vector;

public class UtilesXerador {
    protected static Log m_Log = LogFactory.getLog(UtilesXerador.class.getName()); // Para información de logs

    protected UtilesXerador() {
      super();
    }

    public static es.altia.util.conexion.Cursor ConsultaAplicacionesUsuario(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;
		String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);
		con = adapBD.getConnection();
		String aaaUsu =
			((entrada.get("AAU_USU") != null)
				&& !(entrada.get("AAU_USU").toString().trim().equals("")))
				? entrada.get("AAU_USU").toString().trim()
				: null;
		String aidIdi =
			((entrada.get("AID_IDI") != null)
				&& (!((((String) entrada.get("AID_IDI")).equals("")))))
				? entrada.get("AID_IDI").toString()
				: "null";
		String traerGen =
			((entrada.get("TRAERGEN") != null)
				&& !(entrada.get("TRAERGEN").toString().trim().equals("")))
				? entrada.get("TRAERGEN").toString().trim()
				: null;

		String aplCodGenerador = "8";

		try {
			// Definir consulta.
			String consulta =
				"SELECT APL_COD, AID_TEX FROM " + GlobalNames.ESQUEMA_GENERICO + "A_APL A_APL," +
                        GlobalNames.ESQUEMA_GENERICO + "A_AAU A_AAU," + GlobalNames.ESQUEMA_GENERICO +
                        "A_AID A_AID WHERE APL_COD = AAU_APL AND APL_COD = AID_APL ";
			consulta += " AND AAU_USU = ? AND AID_IDI = ?";

			if ((traerGen != null) && (traerGen.equals("N")))
				consulta += " AND APL_COD <> " + aplCodGenerador;

			consulta += " ORDER BY 2";
			pstmt = con.prepareStatement(consulta);
			pstmt.setInt(1, Integer.parseInt(aaaUsu));
			pstmt.setInt(2, Integer.parseInt(aidIdi));
			// Ejecutar la consulta.
			m_Log.debug("Antes de ejec la query.");
            if (m_Log.isDebugEnabled()) m_Log.debug(consulta);
            rs = pstmt.executeQuery();
			// Procesar el resultado de la consulta.
			m_Log.debug("Despues de ejec la query.");
			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());
			pstmt.close();
			//adapBD.devolverConexion(con);
			return datosResultado;
		} catch (Exception e) {
            e.printStackTrace();
            throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaCamposInformeEntidad(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta = "select CI.CODIGO AS COD_CAMPOINFORME,";
			consulta
				+= "CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,CI.NOMEAS AS NOMEAS  ";
			consulta
				+= " FROM ENTIDADEINFORME EI,CAMPOENTIDADEINFORME CEI,CAMPOINFORME CI";

			consulta += " WHERE   EI.CODIGO = CEI.COD_ENTIDADEINFORME ";
			consulta += " AND CEI.COD_CAMPOINFORME = CI.CODIGO ";
			consulta += " AND EI.CODIGO= " + entrada.get("COD_ENTIDADEINFORME");
			consulta += " ORDER BY CI.NOME";

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es
		.altia
		.util
		.conexion
		.Cursor ConsultaCamposPorEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta =
				"select CI.CODIGO AS CODIGO,CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,CI.SELECTVALORES AS SELECTVALORES,CI.FROMVALORES AS FROMVALORES,CI.WHEREVALORES AS WHEREVALORES FROM CAMPOENTIDADEINFORME CEI,CAMPOINFORME CI WHERE CEI.COD_CAMPOINFORME=CI.CODIGO AND CEI.COD_ENTIDADEINFORME="
					+ entrada.get("COD_ENTIDADEINFORME");

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es
		.altia
		.util
		.conexion
		.Cursor ConsultaClausulasEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta =
				"select CODIGO,NOME,CLAUSULAFROM,CLAUSULAWHERE FROM ENTIDADEINFORME WHERE CODIGO="
					+ entrada.get("COD_ENTIDADEINFORME");

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaDatosCamposXerador(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.HashtableWithNull temp = null;

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			int cont = 0;

			java.util.Collection col =
				(java.util.Collection) entrada.get("CAMPOSSELECCION");
			if (col != null) {
				java.util.Iterator iter = col.iterator();

				// Definir consulta.
				String consulta =
					"select CODIGO,NOME,CAMPO,TIPO,LONXITUDE,NOMEAS FROM CAMPOINFORME WHERE CODIGO IN (";
				while (iter.hasNext()) {
					temp = (es.altia.util.HashtableWithNull) iter.next();
					if (cont > 0)
						consulta += ",";
					cont++;
					consulta += temp.get("COD_CAMPOINFORME");
				}
				consulta += ")";
				pstmt = con.prepareStatement(consulta);
                m_Log.debug("Consulta: "+consulta);

				// Ejecutar la consulta.

				rs = pstmt.executeQuery();

				// Procesar el resultado de la consulta.

				datosResultado =
					new es.altia.util.conexion.Cursor(
						rs,
						rs.getMetaData().getColumnCount());

				pstmt.close();
				//adapBD.devolverConexion(con);

			}
			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaDatosCampoXerador(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		if (con == null)
			con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta =
				"select CODIGO,NOME,CAMPO,TIPO,LONXITUDE,SELECTVALORES,FROMVALORES,WHEREVALORES,NOMEAS FROM CAMPOINFORME WHERE CODIGO = ";

			consulta += entrada.get("COD_CAMPOINFORME");
			m_Log.debug("");
			m_Log.debug("sql ConsultaDatosCampoXerador en UtilesXerador: " + consulta);
			m_Log.debug("");
			pstmt = con.prepareStatement(consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaDatosEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		if (con == null)
			con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta =
				"select CODIGO,NOME,CLAUSULAFROM,CLAUSULAWHERE,NOMEVISTA FROM ENTIDADEINFORME WHERE CODIGO = ";
			consulta += entrada.get("COD_ENTIDADEINFORME");
            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de consultaDatosEntidadeInforme es : " + consulta);

			// Ejecutar la consulta.

			pstmt = con.prepareStatement(consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}
	public static es.altia.util.conexion.Cursor ConsultaEntidadesCamposInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		String mnucod =
			((entrada.get("MNUCOD") != null)
				&& !(entrada.get("MNUCOD").toString().trim().equals("")))
				? entrada.get("MNUCOD").toString().trim()
				: null;
		String apl_cod =
			((entrada.get("APL_COD") != null)
				&& (!((((String) entrada.get("APL_COD")).equals("")))))
				? entrada.get("APL_COD").toString()
				: "null";

		try {

			// Definir consulta.
			String consulta =
				"select EI.CODIGO AS CODIGO,EI.NOME AS NOME,CI.CODIGO AS COD_CAMPOINFORME,";
			consulta
				+= "CI.NOME AS NOMECAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE  ";
			consulta
				+= " FROM ENTIDADEINFORME EI,CAMPOENTIDADEINFORME CEI,CAMPOINFORME CI";
			if (apl_cod != null)
				consulta += ",ENTIDADEAPLICACION EA ";
			consulta += " WHERE   EI.CODIGO = CEI.COD_ENTIDADEINFORME ";
			consulta += " AND CEI.COD_CAMPOINFORME = CI.CODIGO ";
			if (apl_cod != null) {

				consulta += " AND EI.CODIGO = EA.COD_ENTIDADEINFORME "
					+ " AND EA.APL_COD = "
					+ apl_cod;
			}
			consulta += " ORDER BY EI.NOME,CI.NOME";
            if(m_Log.isDebugEnabled()) m_Log.debug("UtilesXerador : " + consulta);

			pstmt = con.prepareStatement(consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaEntidadesInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;
		String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);
		con = adapBD.getConnection();

		try {
			// Definir consulta.
			String consulta =
				"SELECT CODIGO,NOME,NOMEVISTA FROM ENTIDADEINFORME ";
			if (entrada.get("COD_ENTIDADEINFORME") != null)
				consulta += " WHERE CODIGO <> "
					+ entrada.get("COD_ENTIDADEINFORME");
			consulta += " ORDER BY 2";
			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();
			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			adapBD.devolverConexion(con);
			return datosResultado;
		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaEstructurasInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;
		String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);
		con = adapBD.getConnection();

		try {
			// Definir consulta.

			String consulta =
				" SELECT COD_ESTRUCTURA,A_DOC.DOC_NOM AS NOME,POSICION FROM ESTRUCTURAINFORME,A_DOC ";
			consulta += " WHERE A_DOC.DOC_APL = " + entrada.get("APL_COD");
			if (entrada.get("COD_ENTIDADEINFORME") != null)
				consulta += " AND ESTRUCTURAINFORME.COD_ENTIDADEINFORME <> "
					+ entrada.get("COD_ENTIDADEINFORME");
			consulta += " AND A_DOC.DOC_CEI=ESTRUCTURAINFORME.COD_ESTRUCTURA ";
			if (entrada.get("COD_ESTRUCTURA") != null)
				consulta += " AND ESTRUCTURAINFORME.COD_ESTRUCTURA <> "
					+ entrada.get("COD_ESTRUCTURA");
			consulta += " ORDER BY 2";

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();
			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			adapBD.devolverConexion(con);
			return datosResultado;
		} catch (Exception e) {
			throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaFontesInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			con = adapBD.getConnection();

			// Definir consulta.
			String consulta =
				"select FL.COD_FONTELETRA AS COD_FONTELETRA,FL.NOME AS NOME";
			consulta += " FROM FONTELETRA FL";
			consulta += " ORDER BY FL.NOME";

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaNomeFonte(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {

			con = adapBD.getConnection();

			// Definir consulta.
			String consulta =
				"select COD_FONTELETRA,NOME FROM FONTELETRA WHERE COD_FONTELETRA = ";
			consulta += entrada.get("COD_FONTELETRA");
			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es
		.altia
		.util
		.conexion
		.Cursor ConsultaSubEntidadesPorEntidadeInforme(
			es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		boolean eraNull = false;
		if (con == null) {
			eraNull = true;
			con = adapBD.getConnection();
		}
		try {

			// Definir consulta.
			String consulta =
				"select EI.CODIGO AS CODIGO,EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM ,EI.CLAUSULAWHERE AS CLAUSULAWHERE,EI.NOMEVISTA AS NOMEVISTA FROM ENTIDADEINFORME EI,ENTSUBENT ES,ENTSUBENT ES2";
			consulta += "  WHERE ES2.COD_ENTIDADEINFORME="
				+ entrada.get("COD_ENTIDADEINFORME");
			consulta += " AND EI.CODIGO=ES.COD_ENTIDADEINFORME";

			consulta += " AND ES.COD_PAI = ES2.COD_ENTSUBENT";

			consulta += " ORDER BY 1 ";
			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			if (eraNull)
				adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaTienePlantillaInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;
		String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);
		con = adapBD.getConnection();
		String cod =
			((entrada.get("COD_INFORMEXERADOR") != null)
				&& !(entrada
					.get("COD_INFORMEXERADOR")
					.toString()
					.trim()
					.equals("")))
				? entrada.get("COD_INFORMEXERADOR").toString().trim()
				: null;
		try {
			// Definir consulta.
			String consulta =
				"SELECT 'X' FROM INFORMEXERADOR IX WHERE IX.F_DOT IS NOT NULL AND IX.CODIGO = ?";
			pstmt = con.prepareStatement(consulta);
			pstmt.setString(1, cod);
			// Ejecutar la consulta.
			m_Log.debug("Antes de ejec la query.");
            if (m_Log.isDebugEnabled()) m_Log.debug(consulta);
			rs = pstmt.executeQuery();
			// Procesar el resultado de la consulta.
			m_Log.debug("Despues de ejec la query.");
			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());
			pstmt.close();
			adapBD.devolverConexion(con);
			return datosResultado;
		} catch (Exception e) {            
            throw e;
		} finally {
			adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaTiposEtiquetaInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			con = adapBD.getConnection();

			// Definir consulta.
			String consulta =
				"select CODIGO,CENCOD,NOME,ANCHO,ALTO, ESPACIOHORIZONTAL, ESPACIOVERTICAL,MARXESUPERIOR, MARXEINFERIOR, MARXEESQUERDA, MARXEDEREITA, MARXESUPERIORETIQUETA, MARXEESQUERDAETIQUETA,BORDEETIQUETA ";
			consulta += " FROM ETIQUETA ET";
			consulta += " WHERE ET.APL_COD =" + entrada.get("APL_COD");
			consulta += " AND ET.MUN_PAI =" + entrada.get("MUN_PAI");
			consulta += " AND ET.MUN_PRV =" + entrada.get("MUN_PRV");
			consulta += " AND ET.MUN_COD =" + entrada.get("MUN_COD");

			consulta += " ORDER BY ET.NOME";

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	public static es.altia.util.conexion.Cursor ConsultaValoresCampoInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		java.sql.Connection con = null;

		String[] _params = (String[]) entrada.get("PARAMS");

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		con = adapBD.getConnection();

		try {

			// Definir consulta.
			String consulta =
				"select distinct "
					+ entrada.get("CAMPOSELECT")
					+ " FROM "
					+ entrada.get("CLAUSULAFROM")
					+ " WHERE "
					+ entrada.get("CLAUSULAWHERE");
			consulta += " ORDER BY ";
			if (entrada.get("CLAUSULAORDER") != null)
				consulta += entrada.get("CLAUSULAORDER");
			else
				consulta += " 1 ";

			// Ejecutar la consulta.

			pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);

			// Ejecutar la consulta.

			rs = pstmt.executeQuery();

			// Procesar el resultado de la consulta.

			datosResultado =
				new es.altia.util.conexion.Cursor(
					rs,
					rs.getMetaData().getColumnCount());

			pstmt.close();
			//adapBD.devolverConexion(con);

			return datosResultado;

		} catch (Exception e) {
			throw e;
		} finally {
			//adapBD.devolverConexion(con);
		}

	}

	/**
	 * Method ejecutaConsulta.
	 * @param entrada  hashTable conteniendo "CONSULTA" y "PARAMETROS"
	 * @return Cursor
	 * @throws Exception Si ocurre algun error
	 */
	public static Cursor ejecutaConsulta(HashtableWithNull entrada) throws Exception {

        Cursor datosResultado;

        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        Connection con;

        String[] _params = (String[]) entrada.get("PARAMS");

        if (_params == null) {
            throw new Exception("NO SE HAN ESPECIFICADO PARAMETROS DE CONEXION");
        }
        AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

        con = adapBD.getConnection();

        try {
            // Definir consulta.
            String consulta = (String) entrada.get("CONSULTA");
            m_Log.debug("CONSULTA A EJECUTAR: " + consulta);
            pstmt = con.prepareStatement(consulta);
            Vector parametros = (Vector) entrada.get("PARAMETROS");
            if ((parametros != null) && (parametros.size() > 0)) {
                java.util.Iterator iterParam = parametros.iterator();
                int cont = 1;
                while (iterParam.hasNext()) {
                    String parametro = iterParam.next().toString();
                    if (parametro.equals("")) {
                        pstmt.setNull(cont, Types.VARCHAR);
                    } else {
                        pstmt.setString(cont, parametro);
                    }
                    cont++;
                }
            } // Ejecutar la consulta.
            m_Log.debug("CONSULTA A EJECUTAR: " + consulta);
            rs = pstmt.executeQuery();
            // Procesar el resultado de la consulta.
            datosResultado = new Cursor(rs, rs.getMetaData().getColumnCount());
            return datosResultado;
        } catch (Exception e) {
            throw e;
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(pstmt);
            SigpGeneralOperations.devolverConexion(adapBD, con);

        }

    }
}