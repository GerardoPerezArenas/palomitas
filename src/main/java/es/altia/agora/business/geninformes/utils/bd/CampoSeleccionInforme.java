package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CampoSeleccionInforme {
	private java.sql.Connection con = null;
	private String[] _params = null;
    protected static Log m_Log =
            LogFactory.getLog(CampoSeleccionInforme.class.getName());

    public CampoSeleccionInforme(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaCampoSeleccionInforme(
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
			"select   CSI.COD_ESTRUCTURA AS COD_ESTRUCTURA,CSI.POSICION AS POSICION,CSI.COD_CAMPOINFORME AS COD_CAMPOINFORME,CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,CSI.ANCHO AS ANCHO ";
		consulta += " FROM CAMPOSELECCIONINFORME CSI, CAMPOINFORME CI ";
		consulta += " WHERE CSI.COD_CAMPOINFORME = CI.CODIGO ";
		consulta += " AND CSI.COD_ESTRUCTURA = "
			+ usuario.get("COD_ESTRUCTURA");
		consulta += " ORDER BY CSI.POSICION";
		
		m_Log.debug("CampoSeleccionInforme:ConsultaCampoSeleccionInforme:Sql: " + consulta);

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

	public void AltaCampoSeleccionInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

		//con = adapBD.getConnection();

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

		String ancho =
			((usuario.get("ANCHO") != null)
				&& (!(((usuario.get("ANCHO").toString()).equals("")))))
				? usuario.get("ANCHO").toString()
				: "null";

		// Definir consulta.

		String consulta = "insert ";
		consulta
			+= " into CAMPOSELECCIONINFORME ( COD_ESTRUCTURA,POSICION,COD_CAMPOINFORME,ANCHO) ";
		consulta += " values ("
			+ cod_estructura
			+ ","
			+ posicion
			+ ","
			+ cod_campoinforme
			+ ","
			+ ancho
			+ " ) ";

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

	public void BorraCampoSeleccionInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;

		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);


		//con = adapBD.getConnection();
		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   CAMPOSELECCIONINFORME ";
		consulta += " WHERE COD_ESTRUCTURA = "
			+ usuario.get("COD_ESTRUCTURA");

		pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}