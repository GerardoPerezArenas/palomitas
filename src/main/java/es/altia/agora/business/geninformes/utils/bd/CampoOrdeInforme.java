package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CampoOrdeInforme {
	private java.sql.Connection con = null;
	private String[] _params = null;
    protected static Log m_Log =
                LogFactory.getLog(CampoOrdeInforme.class.getName());

    public CampoOrdeInforme(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaCampoOrdeInforme(
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
			"select   COI.COD_ESTRUCTURA AS COD_ESTRUCTURA,COI.POSICION AS POSICION,COI.COD_CAMPOINFORME AS COD_CAMPOINFORME,CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,COI.TIPOORDE AS TIPOORDE ";
		consulta += " FROM CAMPOORDEINFORME COI, CAMPOINFORME CI ";
		consulta += " WHERE COI.COD_CAMPOINFORME = CI.CODIGO ";
		consulta += " AND COI.COD_ESTRUCTURA = "
			+ usuario.get("COD_ESTRUCTURA");
		
		m_Log.debug("CampoOrdeInforme:ConsultaCampoOrdeInforme:Sql: " + consulta);

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

	}

	public void AltaCampoOrdeInforme(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = null;
		if (_params!=null) adapBD=new AdaptadorSQLBD(_params);


		//con = adapBD.getConnection();

		String cod_estructura =
			((usuario.get("COD_ESTRUCTURA") != null)
				&& (!(((usuario.get("COD_ESTRUCTURA").toString())
					.equals("")))))
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

		String tipoOrde =
			((usuario.get("TIPOORDE") != null)
				&& (!((((String) usuario.get("TIPOORDE")).equals("")))))
				? "'" + usuario.get("TIPOORDE").toString() + "'"
				: "null";

		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into CAMPOORDEINFORME ( COD_ESTRUCTURA,POSICION,COD_CAMPOINFORME,TIPOORDE) ";
		consulta += " values ("
			+ cod_estructura
			+ ","
			+ posicion
			+ ","
			+ cod_campoinforme
			+ ","
			+ tipoOrde
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void BorraCampoOrdeInforme(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");
		
		AdaptadorSQLBD adapBD = null;

		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);


		//con = adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   CAMPOORDEINFORME ";
		consulta += " WHERE COD_ESTRUCTURA = "
			+ usuario.get("COD_ESTRUCTURA");

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}