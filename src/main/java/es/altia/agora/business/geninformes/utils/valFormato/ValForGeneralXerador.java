package es.altia.agora.business.geninformes.utils.valFormato;

public final class ValForGeneralXerador {
	public static void ValidaFormatoTamanoFonte(String nome) throws Exception {
		if (nome != null) {
			if ((nome.equals("")) || (nome.length() > 2))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2004);
			else {
				try {

					Integer.parseInt(nome);
				} catch (Exception e) {
					throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2004);
				}

			}
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2004);
	}

	public static void ValidaFormatoOpcionSN(String trat) throws Exception {
		if ((trat == null) || (trat.trim().equals("")))
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2005);
		else if ((!(trat.equals("S"))) && (!(trat.equals("N"))))
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2005);
	}

	public static void ValidaFormatoMedidaCm(String nome) throws Exception {
		float f = 0.0F;

		if (nome != null) {
			if ((nome.equals("")) || (nome.length() > 5))
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2006);
			else {
				try {

					f = Float.parseFloat(nome);
					if (!((f >= 0.0F) && (f < 100.0F)))
						throw new Exception();
				} catch (Exception e) {
					throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2006);
				}

			}
		} else
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(2006);
	}

}
