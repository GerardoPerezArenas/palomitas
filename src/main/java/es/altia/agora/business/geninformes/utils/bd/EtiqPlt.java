package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class EtiqPlt {
	private java.sql.Connection con = null;
	private String[] _params = null;
    protected static Log m_Log = LogFactory.getLog(EtiqPlt.class.getName());; // Para información de logs

	public EtiqPlt(java.sql.Connection con) {
		this.con = con;
	}

	public es.altia.util.conexion.Cursor ConsultaEtiquetasPorInforme(
		es.altia.util.HashtableWithNull usuario)
		throws Exception {

		es.altia.util.conexion.Cursor datosResultado = null;

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		boolean conEraNull = false;
		if (con == null) {
			conEraNull = true;
			con = adapBD.getConnection();
		}
		// Definir consulta.
		String separador =
			(usuario.get("SEPARADOR") != null)
				? usuario.get("SEPARADOR").toString()
				: "_";

        String consulta =
                "select " + adapBD.funcionCadena(adapBD.FUNCIONCADENA_CONCAT,
                            new String[]{"ETP.PREFIJO","'"+separador+"'","CI.NOMEAS"}) + " AS CODIGO," +
				 "CI.NOME AS NOME";
		consulta += " FROM ETIQ_PLT ETP,CAMPOINFORME CI ";
		consulta += " WHERE ETP.COD_CAMPOINFORME=CI.CODIGO ";
		consulta += " AND ETP.COD_INFORMEXERADOR =  "
			+ usuario.get("COD_INFORMEXERADOR");
		consulta += " ORDER BY 2 ";
        if (m_Log.isDebugEnabled()) m_Log.debug("Consulta es:"+consulta+".");
		pstmt = con.prepareStatement(consulta);

		// Ejecutar la consulta.

		rs = pstmt.executeQuery();

		// Procesar el resultado de la consulta.

		datosResultado =
			new es.altia.util.conexion.Cursor(
				rs,
				rs.getMetaData().getColumnCount());

		pstmt.close();
		if (conEraNull)
			adapBD.devolverConexion(con);

		return datosResultado;

	}

	public void AltaEtiqPlt(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		if (con == null)
			con = adapBD.getConnection();

		Long salida = null;

		String codCampoInforme =
			((usuario.get("COD_CAMPOINFORME") != null)
				&& (!((((String) usuario.get("COD_CAMPOINFORME")).equals("")))))
				? usuario.get("COD_CAMPOINFORME").toString()
				: "null";
		//m_Log.debug("en EtiqPlt: codCampoInforme " + codCampoInforme);

		String codInformeXerador =
			((usuario.get("COD_INFORMEXERADOR") != null)
				&& (!((((String) usuario.get("COD_INFORMEXERADOR")).equals("")))))
				? usuario.get("COD_INFORMEXERADOR").toString()
				: "null";
		//m_Log.debug("en EtiqPlt: codInformeXerador " + codInformeXerador);

		String prefijo =
			((usuario.get("PREFIJO") != null)
				&& (!(((usuario.get("PREFIJO")).toString().equals("")))))
				? usuario.get("PREFIJO").toString()
				: "null";
		//m_Log.debug("en EtiqPlt: prefijo " + prefijo);

		// Definir consulta.
	  String sql = "SELECT * FROM ETIQ_PLT WHERE COD_CAMPOINFORME =" + codCampoInforme + 
	  						 " AND COD_INFORMEXERADOR =" + codInformeXerador + " AND PREFIJO ='" +
	  						 prefijo + "'";
	  if (m_Log.isDebugEnabled()) m_Log.debug("en EtiqPlt : " + sql);
	  pstmt = con.prepareStatement(sql); 
	  rs = pstmt.executeQuery();
	  String existe = "no";
	  if(rs.next()) {
	  	existe = "si";
	  }

		if("no".equals(existe)) {
			String consulta = "insert ";
			consulta
				+= " into ETIQ_PLT ( COD_CAMPOINFORME, COD_INFORMEXERADOR,PREFIJO) ";
			consulta += " values ("
				+ codCampoInforme
				+ ","
				+ codInformeXerador
				+ ",?) ";
			if (m_Log.isDebugEnabled()) m_Log.debug("en EtiqPlt: consulta " + consulta);
	
			pstmt = con.prepareStatement(consulta);
			pstmt.setString(1, prefijo);
			// Ejecutar la consulta.
	
			pstmt.executeUpdate();
	  }

		pstmt.close();

		//adapBD.devolverConexion(con);

	}

	public void BorraEtiqPlt(es.altia.util.HashtableWithNull usuario)
		throws Exception {

		java.sql.PreparedStatement pstmt = null;

		_params = (String[]) usuario.get("PARAMS");

		AdaptadorSQLBD adapBD = null;
		if (_params != null)
			adapBD = new AdaptadorSQLBD(_params);

		if (con == null)
			con = adapBD.getConnection();

		// Definir consulta.
		String consulta = "delete ";
		consulta += "  from   ETIQ_PLT ";
		consulta += " WHERE COD_INFORMEXERADOR  = "
			+ usuario.get("COD_INFORMEXERADOR");

		pstmt = con.prepareStatement(consulta);

		// Ejecutar la consulta.

		pstmt.executeUpdate();

		// Procesar el resultado de la consulta.

		pstmt.close();
		//adapBD.devolverConexion(con);

	}

}
