/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.sir.soap.mapper;

import es.altia.flexia.sir.soap.generated.TipoRespuesta;

/**
 *
 * @author Juangc
 */
public class RespuestaAsientoMapper {
	
	public RespuestaAsientoMapper(){}
	
	public es.altia.flexia.sir.soap.generated.RespuestaAsiento RespuestaAsientoFlexiaToRespuestaAsientoSIR(
			es.altia.flexia.sir.model.RespuestaAsiento respuestaAsiento){
		es.altia.flexia.sir.soap.generated.RespuestaAsiento respuesta = new es.altia.flexia.sir.soap.generated.RespuestaAsiento();
		
		respuesta.setAppVersion(respuestaAsiento.getAppVersion());
		respuesta.setCodAsiento(respuestaAsiento.getCodAsiento());
		respuesta.setCodEntidadRegistralDestino(respuestaAsiento.getCodEntidadRegistralDestino());
		respuesta.setCodEntidadRegistralProcesa(respuestaAsiento.getCodEntidadRegistralProcesa());
		respuesta.setCodIntercambio(respuestaAsiento.getCodIntercambio());
		respuesta.setCodUnidadTramitadoraDestino(respuestaAsiento.getCodUnidadTramitadoraDestino());
		respuesta.setContactoUsuario(respuestaAsiento.getContactoUsuario());
		respuesta.setDescripcionEntidadRegistralDestino(respuestaAsiento.getDescripcionEntidadRegistralDestino());
		respuesta.setDescripcionUnidadTramitadoraDestino(respuestaAsiento.getDescripcionUnidadTramitadoraDestino());
		
		respuesta.setFeEntradaDestino(null);
		respuesta.setMotivo(respuestaAsiento.getMotivo());
		respuesta.setNombreUsuario(respuestaAsiento.getNombreUsuario());
		
		respuesta.setNuRgEntradaDestino(null);
		
		if(respuestaAsiento.getTipoRespuesta().getValor().equals(TipoRespuesta.CONFIRMACION.value())){
			respuesta.setTipoRespuesta(TipoRespuesta.CONFIRMACION);
		}else if (respuestaAsiento.getTipoRespuesta().getValor().equals(TipoRespuesta.RECHAZO.value())) {
			respuesta.setTipoRespuesta(TipoRespuesta.RECHAZO);
		}
		return respuesta;
		
	}
}
