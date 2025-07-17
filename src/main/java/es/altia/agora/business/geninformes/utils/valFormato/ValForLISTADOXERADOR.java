package es.altia.agora.business.geninformes.utils.valFormato; 

public final class ValForLISTADOXERADOR {

	public static void ValidaFormatoOrientacion(String gasto)
		throws Exception {
		if (gasto != null) {
			if ((!(gasto.equals("A"))) && (!(gasto.equals("V"))))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2011);
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2011);

	}

}
