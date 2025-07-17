package es.altia.agora.business.geninformes.utils.valRegla;


public final class ValRegXerador {
	/**
	 * Method ValidaExisteEntidadeInforme.
	 * @param varcodentidade
	 * @param conexion
	 * @throws Exception
	 */
	public static void ValidaExisteEntidadeInforme(
		String varcodentidade,
		java.sql.Connection conexion)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
			new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
		es.altia.util.HashtableWithNull hashDatos =
			new es.altia.util.HashtableWithNull();
		hashDatos.put("COD_ENTIDADEINFORME", varcodentidade);
		es.altia.util.conexion.Cursor cursor =
			valida.ExisteCodEntidadeEnENTIDADEINFORME(hashDatos);
		if (cursor.esVacio())
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2000);
	}

	/**
	 * Method ValidaExisteCampoInforme.
	 * @param varcodcampo
	 * @param conexion
	 * @throws Exception
	 */
	public static void ValidaExisteCampoInforme(
		String varcodcampo,
		java.sql.Connection conexion)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
			new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
		es.altia.util.HashtableWithNull hashDatos =
			new es.altia.util.HashtableWithNull();
		hashDatos.put("COD_CAMPOINFORME", varcodcampo);
		es.altia.util.conexion.Cursor cursor =
			valida.ExisteCodCampoEnCAMPOINFORME(hashDatos);
		if (cursor.esVacio())
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2001);
	}

	public static void ValidaCampoEntidadeInforme(
		String varcodentidade,
		String varcodcampo,
		java.sql.Connection conexion)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
			new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
		es.altia.util.HashtableWithNull hashDatos =
			new es.altia.util.HashtableWithNull();
		hashDatos.put("COD_ENTIDADEINFORME", varcodentidade);
		hashDatos.put("COD_CAMPOINFORME", varcodcampo);
		es.altia.util.conexion.Cursor cursor =
			valida.ExisteCampoEntidadeEnCAMPOENTIDADEINFORME(hashDatos);
		if (cursor.esVacio())
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2002);
	}

	public static void ValidaFonte(
		String varcodfonte,
		java.sql.Connection conexion)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
			new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
		es.altia.util.HashtableWithNull hashDatos =
			new es.altia.util.HashtableWithNull();
		hashDatos.put("COD_FONTELETRA", varcodfonte);

		es.altia.util.conexion.Cursor cursor =
			valida.ExisteCodFonteEnFONTELETRA(hashDatos);
		if (cursor.esVacio())
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2003);

	}
	
	
	
	public static void ValidaNonExisteInformeCencodNome(
		String varcencod,
		String varnome,
		java.sql.Connection conexion)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
			new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
		es.altia.util.HashtableWithNull hashDatos =
			new es.altia.util.HashtableWithNull();
		hashDatos.put("CENCOD", varcencod);
		hashDatos.put("NOME", varnome);
		es.altia.util.conexion.Cursor cursor =
			valida.ExisteCenCodNomeEnCAMPOINFORME(hashDatos);
		if (!cursor.esVacio())
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2020);
	}

	
	public static void ValidaNonExisteInformeVinculadoEtiqueta(
			String varCodigoEtiqueta,
	String varcencod,
			java.sql.Connection conexion)
			throws Exception {
			es.altia.agora.business.geninformes.utils.bd.ValidaXerador valida =
				new es.altia.agora.business.geninformes.utils.bd.ValidaXerador(conexion);
			es.altia.util.HashtableWithNull hashDatos =
				new es.altia.util.HashtableWithNull();
			hashDatos.put("CENCOD", varcencod);
			hashDatos.put("CODIGO", varCodigoEtiqueta);
			es.altia.util.conexion.Cursor cursor =
				valida.ExisteInformeVinculadoEtiqueta(hashDatos);
			if (!cursor.esVacio())
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2021);
		}

	
}
