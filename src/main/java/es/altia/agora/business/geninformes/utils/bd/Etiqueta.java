package es.altia.agora.business.geninformes.utils.bd;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.util.conexion.*;

public final class Etiqueta {
	private java.sql.Connection con = null;
	private String[] _params = null;
	

	public Etiqueta(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaEtiquetasCentro(
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
			"select   CODIGO,CENCOD,NOME,ANCHO,ALTO, ESPACIOHORIZONTAL, ESPACIOVERTICAL,MARXESUPERIOR, MARXEINFERIOR, MARXEESQUERDA, MARXEDEREITA, MARXESUPERIORETIQUETA, MARXEESQUERDAETIQUETA,BORDEETIQUETA ";
		consulta += " FROM ETIQUETA EX ";
		consulta += " WHERE EX.CENCOD = '" + usuario.get("CENCOD") + "'";

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

		//	   Ejecutar la consulta.

		rs = pstmt.executeQuery();

		//	   Procesar el resultado de la consulta.

		datosResultado =
			new es.altia.util.conexion.Cursor(
				rs,
				rs.getMetaData().getColumnCount());

		pstmt.close();
		//adapBD.devolverConexion(con);

		return datosResultado;

	}

	public Long AltaEtiqueta(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		String cencod =
			((usuario.get("CENCOD") != null)
				&& (!(((usuario.get("CENCOD").toString()).equals("")))))
				? "'" + usuario.get("CENCOD").toString() + "'"
				: "null";

		String nome =
			((usuario.get("NOME") != null)
				&& (!(((usuario.get("NOME").toString()).equals("")))))
				? "'" + usuario.get("NOME").toString() + "'"
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

        long temp = adapBD.devolverNextValSecuencia(_params, "CONTETIQUETA");
		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into ETIQUETA ( CODIGO,CENCOD,NOME,ANCHO,ALTO, ESPACIOHORIZONTAL, ESPACIOVERTICAL,MARXESUPERIOR, MARXEINFERIOR, MARXEESQUERDA, MARXEDEREITA, MARXESUPERIORETIQUETA, MARXEESQUERDAETIQUETA,BORDEETIQUETA) ";
		consulta += " values (?,"
			+ cencod
			+ ","
			+ nome
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
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
        pstmt.setLong(1, temp);
        // Ejecutar la consulta.
        pstmt.executeUpdate();

//		long temp = Utilidades.insertar(pstmt, "CONTETIQUETA");

		Long salida = new Long(temp);

		pstmt.close();
		//adapBD.devolverConexion(con);
		return salida;
	}

	public void BorraEtiqueta(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   ETIQUETA ";
		consulta += " WHERE ETIQUETA.CODIGO = " + usuario.get("CODIGO");
		consulta += " AND ETIQUETA.CENCOD = '" + usuario.get("CENCOD") + "'";
		pstmt = con.prepareStatement(consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void ActualizaEtiqueta(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();


		String cencod =
			((usuario.get("CENCOD") != null)
				&& (!(((usuario.get("CENCOD").toString()).equals("")))))
				? "'" + usuario.get("CENCOD").toString() + "'"
				: "null";

		String nome =
			((usuario.get("NOME") != null)
				&& (!(((usuario.get("NOME").toString()).equals("")))))
				? "'" + usuario.get("NOME").toString() + "'"
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

		String consulta = "UPDATE ";
		consulta += " ETIQUETA SET  ";
		consulta += " NOME="
			+ nome
			+ ", ANCHO="
			+ ancho
			+ ", ALTO="
			+ alto
			+ ", ESPACIOHORIZONTAL=";
		consulta += espacioHorizontal
			+ ", ESPACIOVERTICAL="
			+ espacioVertical
			+ ", MARXESUPERIOR="
			+ marxeSuperior
			+ ",  MARXEINFERIOR="
			+ marxeInferior
			+ ", MARXEESQUERDA="
			+ marxeEsquerda
			+ ",   MARXEDEREITA="
			+ marxeDereita
			+ ", MARXESUPERIORETIQUETA="
			+ marxeSuperiorEtiqueta
			+ ", MARXEESQUERDAETIQUETA="
			+ marxeEsquerdaEtiqueta
			+ ", BORDEETIQUETA="
			+ bordeEtiqueta
			+ " WHERE CODIGO = "
			+ usuario.get("CODIGO")
			+ " AND CENCOD = '"
			+ usuario.get("CENCOD")
			+ "'";

		pstmt = con.prepareStatement( consulta);
		// Ejecutar la consulta.

		pstmt.executeUpdate();

		//Long salida = new Long(temp);

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}
