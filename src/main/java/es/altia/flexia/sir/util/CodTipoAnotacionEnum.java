/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.sir.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kevin
 */
public enum CodTipoAnotacionEnum {
	PENDIENTE("1"),
	ENVIO("2"),
	REENVIO("03"),
	RECHAZO("04");
	
	private final String codTipoRegistro;
	private static final Map <String, CodTipoAnotacionEnum>listCodTipoAnotacionEnum = new HashMap();
	
	CodTipoAnotacionEnum(String codTipoRegistro) {
		this.codTipoRegistro = codTipoRegistro;
	}
	
	static {
		for (CodTipoAnotacionEnum codTipoAnotacion : CodTipoAnotacionEnum.values()) {
			listCodTipoAnotacionEnum.put(codTipoAnotacion.getCodTipoRegistro(), codTipoAnotacion);
		}
	}
	
	public static String getTextoDescripcion(String codTipoRegistro) {
		switch (getByCodTipoAnotacion(codTipoRegistro)) {
			case PENDIENTE:
				return GestionSirConstantes.TIPO_REGISTRO_PENDIENTE;
			case ENVIO:
				return GestionSirConstantes.TIPO_REGISTRO_ENVIO;
			case REENVIO:
				return GestionSirConstantes.TIPO_REGISTRO_REENVIO;
			case RECHAZO:
				return GestionSirConstantes.TIPO_REGISTRO_RECHAZO;
			default:
				return "";
		}
		
	}
	
	private static CodTipoAnotacionEnum getByCodTipoAnotacion(String codTipoAnotacion) {
		return listCodTipoAnotacionEnum.get(codTipoAnotacion);
	}
	
	public String getCodTipoRegistro() {
		return codTipoRegistro;
	}

}
