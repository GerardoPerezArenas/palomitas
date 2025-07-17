package es.altia.agora.business.geninformes.utils.bd;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class InformeXerador {
	private java.sql.Connection con = null;
	private String[] _params = null;
    protected static Log m_Log = LogFactory.getLog(InformeXerador.class.getName()); // Para información de logs

	public InformeXerador(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaInformesXeradorAplicacion(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		// Definir consulta.

		String consulta =
			"select INFORMEXERADOR.CODIGO AS CODIGO,INFORMEXERADOR.APL_COD AS APL_COD,INFORMEXERADOR.MUN_PAI AS MUN_PAI,INFORMEXERADOR.MUN_PRV AS MUN_PRV,INFORMEXERADOR.MUN_COD AS MUN_COD,INFORMEXERADOR.NOME AS NOME,INFORMEXERADOR.DESCRIPCION as DESCRIPCION,INFORMEXERADOR.FORMATO AS FORMATO,INFORMEXERADOR.COD_ESTRUCTURA AS COD_ESTRUCTURA ";
		consulta += " FROM INFORMEXERADOR ";
		consulta += " WHERE INFORMEXERDOR.APL_COD = " + usuario.get("APL_COD");
		if ((usuario.get("MUN_PAI") != null)
			&& (usuario.get("MUN_PRV") != null)
			&& (usuario.get("MUN_COD") != null)) {
			consulta += " AND INFORMEXERADOR.MUN_PAI = "
				+ usuario.get("MUN_PAI");
			consulta += " AND INFORMEXERADOR.MUN_PRV = "
				+ usuario.get("MUN_PRV");
			consulta += " AND INFORMEXERADOR.MUN_COD = "
				+ usuario.get("MUN_COD");
		}

		consulta += " ORDER BY INFORMEXERADOR.NOME ";
		
		if (m_Log.isDebugEnabled()) m_Log.debug("sql : " + consulta);

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

	}

	public es
		.altia
		.util
		.conexion
		.Cursor ConsultaInformesXeradorAplicacionAdaptada(
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
			"select IX.CODIGO AS CODIGO,IX.NOME AS NOME,IX.DESCRIPCION as DESCRIPCION,IX.PLT_EDITADA AS PLT_EDITADA  ";
		consulta += " FROM INFORMEXERADOR IX ";
		consulta += " WHERE IX.APL_COD = " + usuario.get("APL_COD");
		consulta += " ORDER BY IX.NOME ";
		
        if(m_Log.isDebugEnabled()) m_Log.debug("InformeXerador:ConsultaInformesXeradorAplicacionAdaptada:Sql : " + consulta);

		pstmt = con.prepareStatement(consulta);

		//m_Log.debug("Consulta é:"+consulta+".");

		// Ejecutar la consulta.

		rs = pstmt.executeQuery();

		// Procesar el resultado de la consulta.
		//m_Log.debug("Despues de ejec.");
		datosResultado =
			new es.altia.util.conexion.Cursor(
				rs,
				rs.getMetaData().getColumnCount());

		pstmt.close();
		//adapBD.devolverConexion(con);

		return datosResultado;

	}
	public es.altia.util.conexion.Cursor ConsultaInformeXerador(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		boolean eraNull = false;
		if (con == null) {
			con = adapBD.getConnection();
			eraNull = true;
		}
		// Definir consulta.

		String consulta =
			"select IX.CODIGO AS CODIGO,IX.APL_COD AS APL_COD,IX.MUN_PAI AS MUN_PAI,IX.MUN_PRV AS MUN_PRV,IX.MUN_COD AS MUN_COD,IX.NOME AS NOME,IX.DESCRIPCION as DESCRIPCION,IX.FORMATO AS FORMATO,IX.COD_ESTRUCTURA AS COD_ESTRUCTURA,IX.DTD_CONTIDO AS DTD_CONTIDO,IX.F_DOT AS F_DOT ";
		consulta += " FROM INFORMEXERADOR IX";
		consulta += " WHERE IX.CODIGO = " + usuario.get("COD_INFORMEXERADOR");

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
		if (eraNull)
			adapBD.devolverConexion(con);

		return datosResultado;

	}

	public es.altia.util.conexion.Cursor ConsultaInformeXeradorAdaptada(
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
			"select IX.CODIGO AS CODIGO,IX.APL_COD AS APL_COD,IX.NOME AS NOME,IX.DESCRIPCION as DESCRIPCION,IX.FORMATO AS FORMATO,'CONSULTASQL',ESTI.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,'COD_FUNCION' ";
		consulta += " FROM INFORMEXERADOR IX,ESTRUCTURAINFORME ESTI";
		consulta += " WHERE IX.CODIGO = " + usuario.get("COD_INFORMEXERADOR");
		consulta += " AND IX.COD_ESTRUCTURA=ESTI.COD_ESTRUCTURA ";
		pstmt = con.prepareStatement(consulta);
        if(m_Log.isDebugEnabled()) m_Log.debug("InformeXerador:ConsultaInformeXeradorAdaptada:Sql: " + consulta);

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

	}
	public Long AltaInformeXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		Long salida = null;

		String nome =
			((usuario.get("NOME") != null)
				&& (!((((String) usuario.get("NOME")).equals("")))))
				? (usuario.get("NOME").toString())
				: "null";
		String descripcion =
			(usuario.get("DESCRIPCION") != null)
				? (usuario.get("DESCRIPCION").toString().trim())
				: "null";
		String formato =
			((usuario.get("FORMATO") != null)
				&& (!((((String) usuario.get("FORMATO")).equals("")))))
				? ("'" + usuario.get("FORMATO") + "'")
				: "null";
		String consultasql =
			((usuario.get("CONSULTASQL") != null)
				&& (!((((String) usuario.get("CONSULTASQL")).equals("")))))
				? usuario.get("CONSULTASQL").toString()
				: "null";
		String cod_estructura =
			((usuario.get("COD_ESTRUCTURA") != null)
				&& (!(((usuario.get("COD_ESTRUCTURA"))
					.toString()
					.trim()
					.equals("")))))
				? usuario.get("COD_ESTRUCTURA").toString()
				: "null";

		String dtdContido =
			((usuario.get("DTD_CONTIDO") != null)
				&& (!((((String) usuario.get("DTD_CONTIDO")).equals("")))))
				? usuario.get("DTD_CONTIDO").toString()
				: null;

		String apl_cod =
			((usuario.get("APL_COD") != null)
				&& (!((((String) usuario.get("APL_COD")).equals("")))))
				? usuario.get("APL_COD").toString()
				: "null";

		String mun_pai =
			((usuario.get("MUN_PAI") != null)
				&& (!((((String) usuario.get("MUN_PAI")).equals("")))))
				? usuario.get("MUN_PAI").toString()
				: "null";

		String mun_prv =
			((usuario.get("MUN_PRV") != null)
				&& (!((((String) usuario.get("MUN_PRV")).equals("")))))
				? usuario.get("MUN_PRV").toString()
				: "null";
		String mun_cod =
			((usuario.get("MUN_COD") != null)
				&& (!((((String) usuario.get("MUN_COD")).equals("")))))
				? usuario.get("MUN_COD").toString()
				: "null";

		String plt_Editada =
			((usuario.get("PLT_EDITADA") != null)
				&& (!((((String) usuario.get("PLT_EDITADA")).equals("")))))
				? usuario.get("PLT_EDITADA").toString()
				: null;

        long temp = adapBD.devolverNextValSecuencia(_params, "CONTINFORMEXERADOR");
        //
		// Al crear el informe siempre se mete 'N' en PLT_EDITADA 
		// porque la plantilla se edita después
		//
		String consulta = "insert ";
		consulta
			+= " into INFORMEXERADOR ( CODIGO, APL_COD,MUN_PAI,MUN_PRV,MUN_COD,NOME,DESCRIPCION,FORMATO,COD_ESTRUCTURA,DTD_CONTIDO,PLT_EDITADA) ";
		consulta += " values (?,"+ apl_cod+ ","+ mun_pai+ ","+ mun_prv+ ","+ mun_cod
								+ ",?,?,"+ formato+ ",";
		consulta += cod_estructura + ",?,'N' ) ";

        if(m_Log.isDebugEnabled()) m_Log.debug("InformeXerador:AltaInformeXerador:consulta:"+ consulta+ ".nome:"+ nome+ ".des:"+ descripcion+ "."+"consultasql " + consultasql);
		pstmt = con.prepareStatement(consulta);
        pstmt.setLong(1, temp);
        pstmt.setString(2, nome);
		pstmt.setString(3, descripcion);

		char[] plt_txt =(usuario.get("CONSULTASQL") != null)? ((String) usuario.get("CONSULTASQL")).toCharArray(): null;

		plt_txt =(usuario.get("DTD_CONTIDO") != null)? ((String) usuario.get("DTD_CONTIDO")).toCharArray(): null;

		if (plt_txt == null) {
			pstmt.setNull(4, java.sql.Types.LONGVARCHAR);

		} else {
			java.io.CharArrayReader cr = new java.io.CharArrayReader(plt_txt);
			pstmt.setCharacterStream(4, cr, plt_txt.length);

		}

        pstmt.executeUpdate();
//		long temp = Utilidades.insertar(pstmt, "CONTINFORMEXERADOR");

		salida = new Long(temp);

		pstmt.close();
		return salida;

	}

	public es.altia.util.conexion.Cursor ConsultaPlantillaWordInformeXerador(
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
			"select IX.CODIGO AS CODIGO,IX.APL_COD AS APL_COD,IX.MUN_PAI AS MUN_PAI,IX.MUN_PRV AS MUN_PRV,IX.MUN_COD AS MUN_COD,IX.NOME AS NOME,IX.DESCRIPCION as DESCRIPCION,IX.FORMATO AS FORMATO,IX.COD_ESTRUCTURA AS COD_ESTRUCTURA,IX.F_DOT AS F_DOT ";
		consulta += " FROM INFORMEXERADOR IX";
		consulta += " WHERE IX.CODIGO = " + usuario.get("COD_INFORMEXERADOR");
		
        if(m_Log.isDebugEnabled()) m_Log.debug("sql ConsultaPlantillaWordInformeXerador: " + consulta);

		pstmt = con.prepareStatement(consulta);

		// Ejecutar la consulta.

		rs = pstmt.executeQuery();

		// Procesar el resultado de la consulta.
		//	if (rs.next()) {

		//		byte[] plt_doc = (byte[])rs.getObject("F_DOC");

		//				datos.put("plt_doc",plt_doc);
		//				datos.setString("plt_cod",rs.getString(mTech.getString("SQL.A_PLT.codigo")));
		//				datos.setString("plt_des",rs.getString(mTech.getString("SQL.A_PLT.descripcion")));
		//				datos.setString("plt_apl",rs.getString(mTech.getString("SQL.A_PLT.codigoApli")));

		//				}

		datosResultado =
			new es.altia.util.conexion.Cursor(
				rs,
				rs.getMetaData().getColumnCount());

		pstmt.close();
		//adapBD.devolverConexion(con);

		return datosResultado;

	}

	public void BorraInformeXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);
		//con = adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   INFORMEXERADOR ";
		consulta += " WHERE INFORMEXERADOR.APL_COD = " + usuario.get("APL_COD");
		consulta += " and INFORMEXERADOR.CODIGO = "
			+ usuario.get("COD_INFORMEXERADOR");

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	/**
	 * Method ModificaInformeXerador. Modifica un informe en la BD, solo
	 * modifica la plantilla si esta viene != null
	 * @param usuario
	 * @throws Exception
	 */
	public void ModificaInformeXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {
		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		String codigo =
			((usuario.get("COD_INFORMEXERADOR") != null)
				&& (!((((String) usuario.get("COD_INFORMEXERADOR")).equals("")))))
				? usuario.get("COD_INFORMEXERADOR").toString()
				: "null";

		String nome =
			((usuario.get("NOME") != null)
				&& (!((((String) usuario.get("NOME")).equals("")))))
				? (usuario.get("NOME").toString())
				: "null";
		String descripcion =
			(usuario.get("DESCRIPCION") != null)
				? (usuario.get("DESCRIPCION").toString())
				: "null";
		String formato =
			((usuario.get("FORMATO") != null)
				&& (!((((String) usuario.get("FORMATO")).equals("")))))
				? ("'" + usuario.get("FORMATO") + "'")
				: "null";
		String consultasql =
			((usuario.get("CONSULTASQL") != null)
				&& (!((((String) usuario.get("CONSULTASQL")).equals("")))))
				? usuario.get("CONSULTASQL").toString()
				: "null";
		String cod_entidadeinforme =
			((usuario.get("COD_ENTIDADEINFORME") != null)
				&& (!((((String) usuario.get("COD_ENTIDADEINFORME"))
					.equals("")))))
				? usuario.get("COD_ENTIDADEINFORME").toString()
				: "null";
		String cod_funcion =
			((usuario.get("MNUCOD") != null)
				&& (!((((String) usuario.get("MNUCOD")).equals("")))))
				? "'" + usuario.get("MNUCOD").toString() + "'"
				: "null";

		String cod_estructura =
			((usuario.get("COD_ESTRUCTURA") != null)
				&& (!(((usuario.get("COD_ESTRUCTURA"))
					.toString()
					.trim()
					.equals("")))))
				? usuario.get("COD_ESTRUCTURA").toString()
				: "null";
		String dtdContido =
			((usuario.get("DTD_CONTIDO") != null)
				&& (!((((String) usuario.get("DTD_CONTIDO")).equals("")))))
				? usuario.get("DTD_CONTIDO").toString()
				: null;

		String apl_cod =
			((usuario.get("APL_COD") != null)
				&& (!((((String) usuario.get("APL_COD")).equals("")))))
				? usuario.get("APL_COD").toString()
				: "null";

		String mun_pai =
			((usuario.get("MUN_PAI") != null)
				&& (!((((String) usuario.get("MUN_PAI")).equals("")))))
				? usuario.get("MUN_PAI").toString()
				: "null";

		String mun_prv =
			((usuario.get("MUN_PRV") != null)
				&& (!((((String) usuario.get("MUN_PRV")).equals("")))))
				? usuario.get("MUN_PRV").toString()
				: "null";
		String mun_cod =
			((usuario.get("MUN_COD") != null)
				&& (!((((String) usuario.get("MUN_COD")).equals("")))))
				? usuario.get("MUN_COD").toString()
				: "null";

		byte[] plt_doc = (byte[]) usuario.get("F_DOT");

		String pltEditada = (plt_doc != null) ? "'S'" : "'N'";

		// Definir consulta.
		String consulta = "update INFORMEXERADOR ";
		consulta += "   set NOME= ?,";
		consulta += "       DESCRIPCION = ?,";
		consulta += "       FORMATO = " + formato + ",";
		consulta += "       COD_ESTRUCTURA= " + cod_estructura;

		if (plt_doc != null)
			consulta += ",       F_DOT = ?";
		consulta += " where "; //APL_COD = " + usuario.get("APL_COD");
		consulta += "  CODIGO = " + codigo;
		
        if(m_Log.isDebugEnabled()) m_Log.debug("sql en ModificaInformeXerador " + consulta);

		pstmt = con.prepareStatement(consulta);
		pstmt.setString(1, nome);
		pstmt.setString(2, descripcion);

		char[] plt_txt = null;

		plt_txt =
			(usuario.get("DTD_CONTIDO") != null)
				? ((String) usuario.get("DTD_CONTIDO")).toCharArray()
				: null;

	//	if (plt_txt == null) {
	//		pstmt.setNull(3, java.sql.Types.LONGVARCHAR);
//
//		} else {
//			java.io.CharArrayReader cr = new java.io.CharArrayReader(plt_txt);
//			pstmt.setCharacterStream(3, cr, plt_txt.length);
//
//		}

		if (plt_doc == null) {
			//pstmt.setNull(4, java.sql.Types.BLOB);

		} else {
			java.io.InputStream st = new java.io.ByteArrayInputStream(plt_doc);
			pstmt.setBinaryStream(3, st, plt_doc.length);

		}

		// Ejecutar la consulta.

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void ModificaPlantillaInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {
		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		String codigo =
			((usuario.get("COD_INFORMEXERADOR") != null)
				&& (!((((String) usuario.get("COD_INFORMEXERADOR")).equals("")))))
				? usuario.get("COD_INFORMEXERADOR").toString()
				: "null";

		byte[] plt_doc = (byte[]) usuario.get("F_DOT");

		String pltEditada =
			((plt_doc != null) && (plt_doc.length > 0)) ? "'S'" : "'N'";

		// Definir consulta.
		String consulta = "update INFORMEXERADOR ";
		consulta += "   set ";
		consulta += "       PLT_EDITADA= " + pltEditada;
		if (plt_doc != null)
			consulta += ",       F_DOT = ?";
		consulta += " where  CODIGO = " + codigo;

        if (m_Log.isDebugEnabled()) m_Log.debug(consulta);
        pstmt = con.prepareStatement(consulta);

		if (plt_doc == null) {
			pstmt.setNull(1, java.sql.Types.BLOB);

		} else {
			java.io.InputStream st = new java.io.ByteArrayInputStream(plt_doc);
            pstmt.setBinaryStream(1, st, plt_doc.length);

		}

		// Ejecutar la consulta.

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}