/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.sir.soap.mapper;

import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.TipoDocumentoAnexo;
import es.altia.flexia.sir.model.ValidezDocumento;

/**
 *
 * @author Juangc
 */
public class AnexoMapper {
	public AnexoMapper(){}
	
	public Anexo AnexoSIRToAnexoFlexia(es.altia.flexia.sir.soap.generated.Anexo anexo){
		
		es.altia.flexia.sir.model.Anexo anexoFlexia = new es.altia.flexia.sir.model.Anexo();
		
		anexoFlexia.setCertificado(anexo.getCertificado());
		anexoFlexia.setContenido(anexo.getContenido());
		anexoFlexia.setCsv(anexo.getCsv());
		anexoFlexia.setFirmaDocumento(anexo.getFirmaDocumento());
		anexoFlexia.setHash(anexo.getHash());
		anexoFlexia.setIdentificadorDocumentoFirmado(anexo.getIdentificadorDocumentoFirmado());
		anexoFlexia.setIdentificadorFichero(anexo.getIdentificadorFichero());
		anexoFlexia.setNombreFichero(anexo.getNombreFichero());
		anexoFlexia.setObservaciones(anexo.getObservaciones());
		// anexoFlexia.setTimestamp(Date.valueOf(anexo.getTimeStamp())); TODO Llega como bytes en el XML. Se desconoce el formato de la libreria.
		anexoFlexia.setTipoMIME(anexo.getTipoMIME());
		anexoFlexia.setValidacionOCSPCertificado(anexo.getValidacionOCSPCertificado());
		
		if(anexo.getTipoDocumento().equals(TipoDocumentoAnexo.ADJUNTO_AL_FORMULARIO.getValor())){
			anexoFlexia.setTipoDocumento(TipoDocumentoAnexo.ADJUNTO_AL_FORMULARIO);
		}
		else if (anexo.getTipoDocumento().equals(TipoDocumentoAnexo.FICHERO_TECNICO_INTERNO.getValor())) {
			anexoFlexia.setTipoDocumento(TipoDocumentoAnexo.FICHERO_TECNICO_INTERNO);	
		}
		else if (anexo.getTipoDocumento().equals(TipoDocumentoAnexo.FORMULARIO.getValor())) {
			anexoFlexia.setTipoDocumento(TipoDocumentoAnexo.FORMULARIO);
		}
		else { anexoFlexia.setTipoDocumento(null);}

		
		if(anexo.getValidezDocuemento().equals(ValidezDocumento.COPIA.getValor())){
			anexoFlexia.setValidezDocumento(ValidezDocumento.COPIA);	
		}
		if (anexo.getValidezDocuemento().equals(ValidezDocumento.COPIA_COMPULSADA.getValor())) {
			anexoFlexia.setValidezDocumento(ValidezDocumento.COPIA_COMPULSADA);
		}
		if (anexo.getValidezDocuemento().equals(ValidezDocumento.COPIA_ORIGINAL.getValor())) {
			anexoFlexia.setValidezDocumento(ValidezDocumento.COPIA_ORIGINAL);
		}
		if (anexo.getValidezDocuemento().equals(ValidezDocumento.ORIGINAL.getValor())) {
			anexoFlexia.setValidezDocumento(ValidezDocumento.ORIGINAL);
		}else {anexoFlexia.setValidezDocumento(null);}
		

		
		return anexoFlexia;
	}
	public es.altia.flexia.sir.soap.generated.Anexo AnexoFlexiaToAnexoSIR(Anexo anexo){
		
		es.altia.flexia.sir.soap.generated.Anexo anexoSIR = new es.altia.flexia.sir.soap.generated.Anexo();
		
		anexoSIR.setCertificado(anexo.getCertificado());
		anexoSIR.setContenido(anexo.getContenido());
		anexoSIR.setCsv(anexo.getCsv());
		anexoSIR.setFirmaDocumento(anexo.getFirmaDocumento());
		anexoSIR.setHash(anexo.getHash());
		anexoSIR.setIdentificadorDocumentoFirmado(anexo.getIdentificadorDocumentoFirmado());
		anexoSIR.setIdentificadorFichero(anexo.getIdentificadorFichero());
		anexoSIR.setNombreFichero(anexo.getNombreFichero());
		anexoSIR.setObservaciones(anexo.getObservaciones());
		anexoSIR.setTimeStamp(anexo.getTimestamp().toString());
		anexoSIR.setTipoDocumento(anexo.getTipoDocumento().getValor());
		anexoSIR.setTipoMIME(anexo.getTipoMIME());
		anexoSIR.setValidacionOCSPCertificado(anexo.getValidacionOCSPCertificado());
		anexoSIR.setValidezDocuemento(anexo.getValidezDocumento().getValor());
		
		return anexoSIR;
		
	}
}
