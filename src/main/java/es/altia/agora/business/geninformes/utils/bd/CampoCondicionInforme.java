package es.altia.agora.business.geninformes.utils.bd;

import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CampoCondicionInforme {
	private java.sql.Connection con = null;
	private String[] _params = null;
    protected static Log m_Log =
                LogFactory.getLog(CampoCondicionInforme.class.getName());

    public CampoCondicionInforme(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaCampoCondicionInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		if (con == null)
			con = adapBD.getConnection();

		// Definir consulta.

		String consulta =
			"select   CCI.COD_ESTRUCTURA AS COD_ESTRUCTURA,CCI.POSICION AS POSICION,CCI.CLAUSULA AS CLAUSULA,CCI.COD_CAMPOINFORME AS COD_CAMPOINFORME,CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,CCI.OPERADOR AS OPERADOR,CCI.VALOR AS VALOR,CI.SELECTVALORES AS SELECTVALORES,CI.FROMVALORES AS FROMVALORES,CI.WHEREVALORES AS WHEREVALORES ";
		consulta += " FROM CAMPOCONDICIONINFORME CCI, CAMPOINFORME CI ";
		consulta += " WHERE CCI.COD_CAMPOINFORME = CI.CODIGO ";
		consulta += " AND CCI.COD_ESTRUCTURA = "
			+ usuario.get("COD_ESTRUCTURA");
		consulta += " ORDER BY CCI.POSICION";
		m_Log.debug("CampoCondicionInforme:ConsultaCampoCondicionInforme:Sql: " + consulta);

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		rs = pstmt.executeQuery();

		// Procesar el resultado de la consulta.

		datosResultado =
			new es.altia.util.conexion.Cursor(
				rs,
				rs.getMetaData().getColumnCount());

		//manipBD.cerrarSentenciaPrep(pstmt);

		pstmt.close();
		//////adapBD.devolverConexion(con);

		return datosResultado;

	}

	public void AltaCampoCondicionInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con=adapBD.getConnection();

		String cod_estructura =
			((usuario.get("COD_ESTRUCTURA") != null)
				&& (!(((usuario.get("COD_ESTRUCTURA").toString()).equals("")))))
				? usuario.get("COD_ESTRUCTURA").toString()
				: "null";

		String cod_campoinforme =
			((usuario.get("COD_CAMPOINFORME") != null)
				&& (!(((usuario.get("COD_CAMPOINFORME").toString()).equals("")))))
				? usuario.get("COD_CAMPOINFORME").toString()
				: "null";

		String posicion =
			((usuario.get("POSICION") != null)
				&& (!(((usuario.get("POSICION").toString()).equals("")))))
				? usuario.get("POSICION").toString()
				: "null";

		String clausula =
			((usuario.get("CLAUSULA") != null)
				&& (!((((String) usuario.get("CLAUSULA")).equals("")))))
				? "'" + usuario.get("CLAUSULA").toString() + "'"
				: "null";

		String operador =
			((usuario.get("OPERADOR") != null)
				&& (!((((String) usuario.get("OPERADOR")).equals("")))))
				? "'" + usuario.get("OPERADOR").toString() + "'"
				: "null";

		String valor =
			((usuario.get("VALOR") != null)
				&& (!((((String) usuario.get("VALOR")).trim().equals("")))))
				? usuario.get("VALOR").toString()
				: "null";

		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into CAMPOCONDICIONINFORME ( COD_ESTRUCTURA,POSICION,CLAUSULA,COD_CAMPOINFORME,OPERADOR,VALOR) ";
		consulta += " values ("
			+ cod_estructura
			+ ","
			+ posicion
			+ ","
			+ clausula
			+ ","
			+ cod_campoinforme
			+ ","
			+ operador
			+ ",?"
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
		// Ejecutar la consulta.

		pstmt.setString(1, valor);

		pstmt.executeUpdate();

		pstmt.close();
		//////adapBD.devolverConexion(con);

	}

	public void BorraCampoCondicionInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");
		AdaptadorSQLBD adapBD = null;

		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con=adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   CAMPOCONDICIONINFORME ";
		consulta += " WHERE COD_ESTRUCTURA = " + usuario.get("COD_ESTRUCTURA");

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		////adapBD.devolverConexion(con);

	}

}