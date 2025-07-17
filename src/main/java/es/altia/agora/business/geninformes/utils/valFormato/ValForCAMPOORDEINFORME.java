package es.altia.agora.business.geninformes.utils.valFormato;

public final class ValForCAMPOORDEINFORME {

	public static void ValidaFormatoTipoOrde(String gasto) throws Exception {
		if (gasto != null) {
			if ((!(gasto.equals("A"))) && (!(gasto.equals("D"))))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2010);
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2010);

	}

}
