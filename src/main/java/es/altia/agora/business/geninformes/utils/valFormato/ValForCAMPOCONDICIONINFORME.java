package es.altia.agora.business.geninformes.utils.valFormato;

public final class ValForCAMPOCONDICIONINFORME {
	/**
	 * Method ValidaFormatoPosicion.
	 * @param gasto
	 * @throws Exception
	 */
	public static void ValidaFormatoPosicion(String gasto) throws Exception {
		if (gasto != null) {
			if (gasto.equals(""))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2007);
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2007);

	}

	/**
	 * Method ValidaFormatoClausula.
	 * @param gasto
	 * @throws Exception
	 */
	public static void ValidaFormatoClausula(String gasto) throws Exception {
		if (gasto != null) {
			if ((!(gasto.equals("AND"))) && (!(gasto.equals("OR"))))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2008);
		}

	}

	/**
	 * Method ValidaFormatoOperador.
	 * @param gasto
	 * @throws Exception
	 */
	public static void ValidaFormatoOperador(String gasto) throws Exception {

		boolean atopado = false;
		if (gasto != null) {
			if (gasto.equals(""))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2009);
			else {
				for (int i = 0;
					(i
						< es.altia.agora.business.geninformes.utils
							.ConstantesXerador
							.OPERADORESCONDICIONINFORME
							.length)
						&& (!(atopado));
					i++) {
					atopado =
						gasto.equals(
							es.altia.agora.business.geninformes.utils
								.ConstantesXerador
								.OPERADORESCONDICIONINFORME[i]);
				}
				if (!(atopado))
					throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2009);
			}
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2009);

	}

}
