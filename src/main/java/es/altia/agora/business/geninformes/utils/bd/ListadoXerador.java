package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;

public final class ListadoXerador {
	private java.sql.Connection con = null;
	private String[] _params = null;

	public ListadoXerador(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaListadoXerador(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = null;
		if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

		if (con==null) con = adapBD.getConnection();

		// Definir consulta.

		String consulta =
			"select COD_INFORMEXERADOR, TEXTOCABECEIRA,FONTECABECEIRA,TAMANOFONTECABECEIRA,NEGRITACABECEIRA,SUBRAIADOCABECEIRA,CURSIVACABECEIRA,CABECEIRAOFICIAL,CABECEIRACENTRO,CABECEIRACOLUMNAS,TEXTOPE,FONTEPE,TAMANOFONTEPE,NUMEROPAXINAPE,DATAINFORMEPE,FONTEDETALLE,TAMANOFONTEDETALLE,MARXEESQUERDA,MARXEDEREITA,ORIENTACIONPAXINA,NUMERARLINHAS ";
		consulta += " FROM LISTADOXERADOR ";
		consulta += " WHERE LISTADOXERADOR.COD_INFORMEXERADOR = "
			+ usuario.get("COD_INFORMEXERADOR");

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

	public void AltaListadoXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		String cod_informexerador =
			((usuario.get("COD_INFORMEXERADOR") != null)
				&& (!(((usuario.get("COD_INFORMEXERADOR").toString())
					.equals("")))))
				? usuario.get("COD_INFORMEXERADOR").toString()
				: "null";

		String textoCabeceira =
			(usuario.get("TEXTOCABECEIRA") != null)
				? (usuario.get("TEXTOCABECEIRA").toString())
				: null;

		String fonteCabeceira =
			(usuario.get("FONTECABECEIRA") != null)
				? (usuario.get("FONTECABECEIRA").toString())
				: "null";

		String tamanoFonteCabeceira =
			(usuario.get("TAMANOFONTECABECEIRA") != null)
				? (usuario.get("TAMANOFONTECABECEIRA").toString())
				: "null";

		String negritaCabeceira =
			(usuario.get("NEGRITACABECEIRA") != null)
				? "'" + (usuario.get("NEGRITACABECEIRA").toString()) + "'"
				: "null";

		String subraiadoCabeceira =
			(usuario.get("SUBRAIADOCABECEIRA") != null)
				? "'" + (usuario.get("SUBRAIADOCABECEIRA").toString()) + "'"
				: "null";

		String cursivaCabeceira =
			(usuario.get("CURSIVACABECEIRA") != null)
				? "'" + (usuario.get("CURSIVACABECEIRA").toString()) + "'"
				: "null";

		String cabeceiraOficial =
			(usuario.get("CABECEIRAOFICIAL") != null)
				? "'" + (usuario.get("CABECEIRAOFICIAL").toString()) + "'"
				: "null";

		String cabeceiraCentro =
			(usuario.get("CABECEIRACENTRO") != null)
				? "'" + (usuario.get("CABECEIRACENTRO").toString()) + "'"
				: "null";

		String cabeceiraColumnas =
			(usuario.get("CABECEIRACOLUMNAS") != null)
				? "'" + (usuario.get("CABECEIRACOLUMNAS").toString()) + "'"
				: "null";

		String textoPe =
			(usuario.get("TEXTOPE") != null)
				? (usuario.get("TEXTOPE").toString())
				: null;

		String fontePe =
			(usuario.get("FONTEPE") != null)
				? (usuario.get("FONTEPE").toString())
				: "null";

		String tamanoFontePe =
			(usuario.get("TAMANOFONTEPE") != null)
				? (usuario.get("TAMANOFONTEPE").toString())
				: "null";

		String numeroPaxinaPe =
			((usuario.get("NUMEROPAXINAPE") != null)
				&& (!((((String) usuario.get("NUMEROPAXINAPE")).equals("")))))
				? ("'" + usuario.get("NUMEROPAXINAPE") + "'")
				: "null";

		String dataInformePe =
			((usuario.get("DATAINFORMEPE") != null)
				&& (!((((String) usuario.get("DATAINFORMEPE")).equals("")))))
				? ("'" + usuario.get("DATAINFORMEPE") + "'")
				: "null";

		String fonteDetalle =
			(usuario.get("FONTEDETALLE") != null)
				? (usuario.get("FONTEDETALLE").toString())
				: "null";

		String tamanoFonteDetalle =
			(usuario.get("TAMANOFONTEDETALLE") != null)
				? (usuario.get("TAMANOFONTEDETALLE").toString())
				: "null";

		String marxeEsquerda =
			(usuario.get("MARXEESQUERDA") != null)
				? (usuario.get("MARXEESQUERDA").toString())
				: "null";
		String marxeDereita =
			((usuario.get("MARXEDEREITA") != null)
				&& (!((((String) usuario.get("MARXEDEREITA")).equals("")))))
				? (usuario.get("MARXEDEREITA").toString())
				: "null";

		String orientacionPaxina =
			((usuario.get("ORIENTACIONPAXINA") != null)
				&& (!((((String) usuario.get("ORIENTACIONPAXINA")).equals("")))))
				? ("'" + usuario.get("ORIENTACIONPAXINA") + "'")
				: "null";

		String numerarLinhas =
			((usuario.get("NUMERARLINHAS") != null)
				&& (!((((String) usuario.get("NUMERARLINHAS")).equals("")))))
				? ("'" + usuario.get("NUMERARLINHAS") + "'")
				: "null";

		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into LISTADOXERADOR ( COD_INFORMEXERADOR, TEXTOCABECEIRA,FONTECABECEIRA,TAMANOFONTECABECEIRA,NEGRITACABECEIRA,SUBRAIADOCABECEIRA,CURSIVACABECEIRA,CABECEIRAOFICIAL,CABECEIRACENTRO,CABECEIRACOLUMNAS,TEXTOPE,FONTEPE,TAMANOFONTEPE,NUMEROPAXINAPE,DATAINFORMEPE,FONTEDETALLE,TAMANOFONTEDETALLE,MARXEESQUERDA,MARXEDEREITA,ORIENTACIONPAXINA,NUMERARLINHAS) ";
		consulta += " values ("
			+ cod_informexerador
			+ ",?,"
			+ fonteCabeceira
			+ ","
			+ tamanoFonteCabeceira
			+ ","
			+ negritaCabeceira
			+ ",";
		consulta += subraiadoCabeceira
			+ ", "
			+ cursivaCabeceira
			+ ","
			+ cabeceiraOficial
			+ ","
			+ cabeceiraCentro
			+ ","
			+ cabeceiraColumnas
			+ ",?,"
			+ fontePe
			+ ","
			+ tamanoFontePe
			+ ","
			+ numeroPaxinaPe
			+ ","
			+ dataInformePe
			+ ","
			+ fonteDetalle
			+ ","
			+ tamanoFonteDetalle
			+ ","
			+ marxeEsquerda
			+ ","
			+ marxeDereita
			+ ","
			+ orientacionPaxina
			+ ","
			+ numerarLinhas
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
		pstmt.setString(1, textoCabeceira);
		pstmt.setString(2, textoPe);
		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void BorraListadoXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   LISTADOXERADOR ";
		consulta += " WHERE LISTADOXERADOR.COD_INFORMEXERADOR = "
			+ usuario.get("COD_INFORMEXERADOR");

		pstmt = con.prepareStatement(consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}
