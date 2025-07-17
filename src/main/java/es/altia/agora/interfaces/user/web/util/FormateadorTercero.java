package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.util.StringUtils;
import java.util.HashMap;

public class FormateadorTercero {

	private static TraductorAplicacionBean descriptor = new TraductorAplicacionBean();

	public static String getDescTercero(String nombre, String apellido1, String apellido2, boolean masTerceros) {

		StringBuilder descripcion = new StringBuilder()
				.append(formatearSiNoNulo("%s", apellido1));

		if (descripcion.length() > 0) {
			descripcion.append(" ");
		} 
		descripcion.append(formatearSiNoNulo("%s", apellido2));

		if (descripcion.length() > 0) {
			descripcion.append(", ");
		}
		descripcion.append(formatearSiNoNulo("%s", nombre));

		if (masTerceros) {
			descripcion.append(formatearSiNoNulo(" %s", descriptor.getDescripcion(1, "gEtiq_YOTROS")));
		}

		return descripcion.toString();

	}

	public static String getDescTercero(String nombre, String pa1, String ap1, String pa2, String ap2, boolean masTerceros) {
		String apellido1 = new StringBuilder()
				.append(formatearSiNoNulo("%s", pa1).trim())
				.append(" ")
				.append(formatearSiNoNulo("%s", ap1).trim())
				.toString().trim();

		String apellido2 = new StringBuilder()
				.append(formatearSiNoNulo("%s", pa1).trim())
				.append(" ")
				.append(formatearSiNoNulo("%s", ap1).trim())
				.toString().trim();

		return getDescTercero(nombre, apellido1, apellido2, masTerceros);
	}

	
	public static String formatearDomicilio(TercerosValueObject tercero, DomicilioSimpleValueObject domicilio) {
		StringBuilder domicilioStringBuilder = new StringBuilder()
				.append(formatearSiNoNulo("%s", domicilio.getTipoVia()))
				.append(formatearSiNoNulo(" %s", domicilio.getDescVia()))
				.append(formatearSiNoNulo(" %s", domicilio.getNumDesde()))
				.append(formatearSiNoNulo(" %s", domicilio.getLetraDesde()))
				.append(formatearSiNoNulo(" - %s", domicilio.getNumHasta()))
				.append(formatearSiNoNulo(" %s", domicilio.getLetraHasta()))
				.append(formatearSiNoNulo(" Bl. %s", domicilio.getBloque()))
				.append(formatearSiNoNulo(" Portal %s", domicilio.getPortal()))
				.append(formatearSiNoNulo(" Esc. %s", domicilio.getEscalera()))
				.append(formatearSiNoNulo(" %sº", domicilio.getPlanta()))
				.append(formatearSiNoNulo(" %s", domicilio.getPuerta()))
				.append(formatearSiNoNulo("%s", domicilio.getCodigoPostal()))
				.append(formatearSiNoNulo(" %s", domicilio.getMunicipio()))
				.append(formatearSiNoNulo(" - %s", domicilio.getProvincia()))
				.append(formatearSiNoNulo(" (%s)", domicilio.getPais()));
		return domicilioStringBuilder.toString();
	}
	
	/**
	 * Utiliza los valores de un {@link TercerosValueObject tercero} y de un {@link DomicilioSimpleValueObject
	 * domicilio} para formar una cadena que describe el domicilio del tercero usando los atributos de los respectivos
	 * beans. Las cadenas a null serán ignoradas.
	 *
	 * @param tercero
	 * @param domicilio
	 * @param mostrarPais
	 * @return
	 */
	public static String formatearDomicilioTerceroUOR(TercerosValueObject tercero, DomicilioSimpleValueObject domicilio,
			boolean mostrarPais) {
		HashMap<String, String> datosDomicilio = new HashMap<String, String>();
		datosDomicilio.put("DESCTIPOVIA", domicilio.getTipoVia());
		datosDomicilio.put("DESCVIA", domicilio.getDescVia());
		datosDomicilio.put("NUD", domicilio.getNumDesde());
		datosDomicilio.put("LED", domicilio.getLetraDesde());
		datosDomicilio.put("NUH", domicilio.getNumHasta());
		datosDomicilio.put("LEH", domicilio.getLetraHasta());
		datosDomicilio.put("BLQ", domicilio.getBloque());
		datosDomicilio.put("POR", domicilio.getPortal());
		datosDomicilio.put("ESC", domicilio.getEscalera());
		datosDomicilio.put("PLT", domicilio.getPlanta());
		datosDomicilio.put("PTA", domicilio.getPuerta());
		datosDomicilio.put("CPO", domicilio.getCodigoPostal());
		datosDomicilio.put("PAI_NOM", domicilio.getPais());
		datosDomicilio.put("PRV_NOM", domicilio.getProvincia());
		datosDomicilio.put("MUN_NOM", domicilio.getMunicipio());
		datosDomicilio.put("TER_NOM", tercero.getNombre());
		datosDomicilio.put("TER_PA1", tercero.getPartApellido1());
		datosDomicilio.put("TER_AP1", tercero.getApellido1());
		datosDomicilio.put("TER_AP2", tercero.getPartApellido2());
		datosDomicilio.put("TER_PA2", tercero.getApellido2());

		return formatearDomicilioTerceroUOR(datosDomicilio, mostrarPais);
	}

	public static String formatearDomicilioTerceroUOR(HashMap datosDom, boolean mostrarPais) {
		String descTipoVia = (String) datosDom.get("DESCTIPOVIA");
		String descVia = (String) datosDom.get("DESCVIA");
		String numDesde = (String) datosDom.get("NUD");
		String letraDesde = (String) datosDom.get("LED");
		String numHasta = (String) datosDom.get("NUH");
		String letraHasta = (String) datosDom.get("LEH");
		String bloque = (String) datosDom.get("BLQ");
		String portal = (String) datosDom.get("POR");
		String escalera = (String) datosDom.get("ESC");
		String planta = (String) datosDom.get("PLT");
		String puerta = (String) datosDom.get("PTA");
		String codPostal = (String) datosDom.get("CPO");
		String pais = (String) datosDom.get("PAI_NOM");
		String prov = (String) datosDom.get("PRV_NOM");
		String mun = (String) datosDom.get("MUN_NOM");
		String nombreTER = (String) datosDom.get("TER_NOM");
		String ap1UOR = (String) datosDom.get("TER_AP1");
		String pa1UOR = (String) datosDom.get("TER_PA1");
		String ap2UOR = (String) datosDom.get("TER_AP2");
		String pa2UOR = (String) datosDom.get("TER_PA2");
		String nomCompletoUOR = getDescTercero(nombreTER, pa1UOR, ap1UOR, pa2UOR, ap2UOR, false);
		String mombreUOR = (String) datosDom.get("UOR_NOM");

		StringBuilder domicilio = new StringBuilder()
				.append(formatearSiNoNulo("%s", descTipoVia))
				.append(formatearSiNoNulo(" %s", descVia))
				.append(formatearSiNoNulo(" %s", numDesde))
				.append(formatearSiNoNulo(" %s", letraDesde))
				.append(formatearSiNoNulo(" - %s", numHasta))
				.append(formatearSiNoNulo(" %s", letraHasta))
				.append(formatearSiNoNulo(" Bl. %s", bloque))
				.append(formatearSiNoNulo(" Portal %s", portal))
				.append(formatearSiNoNulo(" Esc. %s", escalera))
				.append(formatearSiNoNulo(" %sº", planta))
				.append(formatearSiNoNulo(" %s", puerta));

		StringBuilder respuesta = new StringBuilder((!nomCompletoUOR.equals("")) ? nomCompletoUOR : mombreUOR);

		respuesta.append("\n")
				.append(formatearSiNoNulo("%s", domicilio.toString()))
				.append(formatearSiNoNulo("\n%s", codPostal))
				.append(formatearSiNoNulo(" %s", mun))
				.append(formatearSiNoNulo(" - %s", prov));

		if (mostrarPais && (pais != null && !pais.equals(""))) {
			respuesta.append(formatearSiNoNulo(" (%s)", pais));
		}

		return respuesta.toString();
	}

	/**
	 * Devuelve una cadena vacía en caso de que la cadena introducida sea null, "null" o "". En caso contrario devuelve
	 * la cadena formateada con formato.
	 *
	 * @param formato
	 * @param valor
	 * @return
	 */
	private static String formatearSiNoNulo(String formato, String valor) {
		if (StringUtils.isNotNullOrEmpty(valor) && !valor.toLowerCase().equals("null")) {
			return String.format(formato, valor);
		}
		return "";
	}
	
	/**
     * Concatena el nombre y los apellidos
     * 
     * @param nombre
     * @param primerApellido
     * @param segundoApellido
     * @return 
     */
    public static final String concatenarNombre(String nombre, String primerApellido, String segundoApellido) {
        String nombreLimpio = org.apache.commons.lang.StringUtils.trimToEmpty(nombre);
        String primerApellidoLimpio = org.apache.commons.lang.StringUtils.trimToEmpty(primerApellido);
        String segundoApellidoLimpio = org.apache.commons.lang.StringUtils.trimToEmpty(segundoApellido);        
        
        String[] nombres = {nombreLimpio, primerApellidoLimpio, segundoApellidoLimpio};
        
        return org.apache.commons.lang.StringUtils.join(nombres, " ");
    }

}
