package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;

public final class EtiquetaXerador {
	private java.sql.Connection con = null;
	private String[] _params = null;

	public EtiquetaXerador(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaEtiquetaXerador(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		// Definir consulta.

		String consulta =
			"select   COD_INFORMEXERADOR,COD_ETIQUETA,ANCHO,ALTO, ESPACIOHORIZONTAL, ESPACIOVERTICAL,MARXESUPERIOR, MARXEINFERIOR, MARXEESQUERDA, MARXEDEREITA, MARXESUPERIORETIQUETA, MARXEESQUERDAETIQUETA,BORDEETIQUETA, FONTEDETALLE, TAMANOFONTEDETALLE";
		consulta += " FROM ETIQUETAXERADOR EX ";
		consulta += " WHERE EX.COD_INFORMEXERADOR = "
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

	public void AltaEtiquetaXerador(es.altia.util.HashtableWithNull usuario)
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
		String codEtiqueta =
			((usuario.get("COD_ETIQUETA") != null)
				&& (!(((usuario.get("COD_ETIQUETA").toString()).equals("")))))
				? usuario.get("COD_ETIQUETA").toString()
				: "null";
		String fonteDetalle =
			((usuario.get("FONTEDETALLE") != null)
				&& (!((((String) usuario.get("FONTEDETALLE")).equals("")))))
				? usuario.get("FONTEDETALLE").toString()
				: "null";

		String tamanoFonteDetalle =
			((usuario.get("TAMANOFONTEDETALLE") != null)
				&& (!((((String) usuario.get("TAMANOFONTEDETALLE")).equals("")))))
				? usuario.get("TAMANOFONTEDETALLE").toString()
				: "null";

		String marxeEsquerda =
			((usuario.get("MARXEESQUERDA") != null)
				&& (!((((String) usuario.get("MARXEESQUERDA")).equals("")))))
				? usuario.get("MARXEESQUERDA").toString()
				: "null";

		String marxeDereita =
			((usuario.get("MARXEDEREITA") != null)
				&& (!((((String) usuario.get("MARXEDEREITA")).equals("")))))
				? usuario.get("MARXEDEREITA").toString()
				: "null";

		String marxeSuperior =
			((usuario.get("MARXESUPERIOR") != null)
				&& (!((((String) usuario.get("MARXESUPERIOR")).equals("")))))
				? usuario.get("MARXESUPERIOR").toString()
				: "null";

		String marxeInferior =
			((usuario.get("MARXEINFERIOR") != null)
				&& (!((((String) usuario.get("MARXEINFERIOR")).equals("")))))
				? usuario.get("MARXEINFERIOR").toString()
				: "null";
		String marxeSuperiorEtiqueta =
			((usuario.get("MARXESUPERIORETIQUETA") != null)
				&& (!((((String) usuario.get("MARXESUPERIORETIQUETA"))
					.equals("")))))
				? usuario.get("MARXESUPERIORETIQUETA").toString()
				: "null";

		String marxeEsquerdaEtiqueta =
			((usuario.get("MARXEESQUERDAETIQUETA") != null)
				&& (!((((String) usuario.get("MARXEESQUERDAETIQUETA"))
					.equals("")))))
				? usuario.get("MARXEESQUERDAETIQUETA").toString()
				: "null";

		String espacioHorizontal =
			((usuario.get("ESPACIOHORIZONTAL") != null)
				&& (!((((String) usuario.get("ESPACIOHORIZONTAL")).equals("")))))
				? usuario.get("ESPACIOHORIZONTAL").toString()
				: "null";

		String espacioVertical =
			((usuario.get("ESPACIOVERTICAL") != null)
				&& (!((((String) usuario.get("ESPACIOVERTICAL")).equals("")))))
				? usuario.get("ESPACIOVERTICAL").toString()
				: "null";

		String bordeEtiqueta =
			((usuario.get("BORDEETIQUETA") != null)
				&& (!((((String) usuario.get("BORDEETIQUETA")).equals("")))))
				? ("'" + usuario.get("BORDEETIQUETA") + "'")
				: "null";

		String ancho =
			((usuario.get("ANCHO") != null)
				&& (!((((String) usuario.get("ANCHO")).equals("")))))
				? usuario.get("ANCHO").toString()
				: "null";
		String alto =
			((usuario.get("ALTO") != null)
				&& (!((((String) usuario.get("ALTO")).equals("")))))
				? usuario.get("ALTO").toString()
				: "null";

		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into ETIQUETAXERADOR ( COD_INFORMEXERADOR,COD_ETIQUETA,ANCHO,ALTO, ESPACIOHORIZONTAL, ESPACIOVERTICAL,MARXESUPERIOR, MARXEINFERIOR, MARXEESQUERDA, MARXEDEREITA, MARXESUPERIORETIQUETA, MARXEESQUERDAETIQUETA,BORDEETIQUETA, FONTEDETALLE, TAMANOFONTEDETALLE) ";
		consulta += " values ("
			+ cod_informexerador
			+ ","
			+ codEtiqueta
			+ ","
			+ ancho
			+ ","
			+ alto
			+ ",";
		consulta += espacioHorizontal
			+ ", "
			+ espacioVertical
			+ ","
			+ marxeSuperior
			+ ","
			+ marxeInferior
			+ ","
			+ marxeEsquerda
			+ ","
			+ marxeDereita
			+ ","
			+ marxeSuperiorEtiqueta
			+ ","
			+ marxeEsquerdaEtiqueta
			+ ","
			+ bordeEtiqueta
			+ ","
			+ fonteDetalle
			+ ","
			+ tamanoFonteDetalle
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void BorraEtiquetaXerador(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();



		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   ETIQUETAXERADOR ";
		consulta += " WHERE ETIQUETAXERADOR.COD_INFORMEXERADOR = "
			+ usuario.get("COD_INFORMEXERADOR");

			pstmt = con.prepareStatement(consulta);
			// Ejecutar la consulta.

			pstmt.executeUpdate();

			pstmt.close();
			//adapBD.devolverConexion(con);

	}

}
