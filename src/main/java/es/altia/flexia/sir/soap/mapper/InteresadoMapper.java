/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.sir.soap.mapper;

import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.model.TipoDocumentoIdentificacion;

/**
 *
 * @author Juangc
 */
public class InteresadoMapper {
	
	public InteresadoMapper(){}
	
	public Interesado interesadoSIRToInteresadoFlexia(es.altia.flexia.sir.soap.generated.Interesado interesado){
		
		Interesado interesadoFlexia = new Interesado();
		Domicilio domicilio = new Domicilio();
		
		domicilio.setCodPostal(interesado.getDomicilio().getCodPostal());
		domicilio.setDireccion(interesado.getDomicilio().getDireccion());
		domicilio.setMunicipio(interesado.getDomicilio().getMunicipio());
		domicilio.setPais(interesado.getDomicilio().getPais());
		domicilio.setProvincia(interesado.getDomicilio().getProvincia());
		
		interesadoFlexia.setCanalPreferenteComunicacion(interesado.getCanalPreferenteComunicacion());
		interesadoFlexia.setCorreoElectronico(interesado.getCorreoElectronico());
		interesadoFlexia.setDireccionElectronicaHabilitada(interesado.getDireccionElectronicaHabilitada());
		interesadoFlexia.setDocumentoIdentificacion(interesado.getDocumentoIdentificacion());
		interesadoFlexia.setDomicilio(domicilio);
		interesadoFlexia.setNombreRazonSocial(interesado.getNombreRazonSocial());
		interesadoFlexia.setObservaciones(interesado.getObservaciones());
		interesadoFlexia.setPrimerApellido(interesado.getPrimerApellido());
		interesadoFlexia.setSegundoApellido(interesado.getSegundoApellido());
		interesadoFlexia.setTelefono(interesado.getTelefono());
		interesadoFlexia.setTipoDocumentoIdentificacion(TipoDocumentoIdentificacion.getEnum(interesado.getTipoDocumentoIdentificacion()));
		return interesadoFlexia;
	}
	
	public es.altia.flexia.sir.soap.generated.Interesado interesadoFlexiaToInteresadoSIR (Interesado interesado){
		
		es.altia.flexia.sir.soap.generated.Interesado interesadoSIR = new es.altia.flexia.sir.soap.generated.Interesado();
		es.altia.flexia.sir.soap.generated.Domicilio domicilio = new es.altia.flexia.sir.soap.generated.Domicilio();
		
		interesadoSIR.setCanalPreferenteComunicacion(interesado.getCanalPreferenteComunicacion());
		interesadoSIR.setCorreoElectronico(interesado.getCorreoElectronico());
		interesadoSIR.setDireccionElectronicaHabilitada(interesado.getDireccionElectronicaHabilitada());
		interesadoSIR.setDocumentoIdentificacion(interesado.getDocumentoIdentificacion());
		
		domicilio.setCodPostal(interesado.getDomicilio().getCodPostal());
		domicilio.setDireccion(interesado.getDomicilio().getDireccion());
		domicilio.setMunicipio(interesado.getDomicilio().getMunicipio());
		domicilio.setPais(interesado.getDomicilio().getPais());
		domicilio.setProvincia(interesado.getDomicilio().getProvincia());
		interesadoSIR.setDomicilio(domicilio);
		
		interesadoSIR.setNombreRazonSocial(interesado.getNombreRazonSocial());
		interesadoSIR.setObservaciones(interesado.getObservaciones());
		interesadoSIR.setPrimerApellido(interesado.getPrimerApellido());
		interesadoSIR.setSegundoApellido(interesado.getSegundoApellido());
		interesadoSIR.setTelefono(interesado.getTelefono());
		interesadoSIR.setTipoDocumentoIdentificacion(interesado.getTipoDocumentoIdentificacion().getValor());
		return interesadoSIR;
	}
}
