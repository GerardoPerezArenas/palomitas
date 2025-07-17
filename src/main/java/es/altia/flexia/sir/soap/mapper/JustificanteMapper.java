/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.sir.soap.mapper;

import es.altia.flexia.sir.model.Justificante;

/**
 *
 * @author Juangc
 */
public class JustificanteMapper {
	
	public es.altia.flexia.sir.soap.generated.Justificante JustificanteFlexiaToJustificanteSIR (Justificante justificante){
		
		es.altia.flexia.sir.soap.generated.Justificante justificanteSIR = new es.altia.flexia.sir.soap.generated.Justificante();
		
		justificanteSIR.setCdIntercambio(justificante.getCodIntercambio());
		justificanteSIR.setCdTpDoc(justificante.getCdTpDoc());
		justificanteSIR.setCdValidez(justificante.getCdValidez());
		justificanteSIR.setContenido(justificante.getContenido());
		justificanteSIR.setCsv(justificante.getCsv());
		justificanteSIR.setDsCertificado(justificante.getDsCertificado());
		justificanteSIR.setDsFirma(justificante.getDsFirma());
		justificanteSIR.setDsHash(justificante.getHash());
		justificanteSIR.setDsTpMime(justificante.getDsTpMime());
		justificanteSIR.setDsValCerficado(justificante.getDsValCertificado());
		justificanteSIR.setFechaHoraPresentacion(justificante.getFechaHoraPresentacion());
		justificanteSIR.setFechaHoraRegistro(justificante.getFechaHoraRegistro());
		justificanteSIR.setHash(justificante.getHash());
		justificanteSIR.setIdFichero(justificante.getIdFichero());
		justificanteSIR.setIsFirmado(justificante.isIsFirmado());
		justificanteSIR.setNombre(justificante.getNombre());
		justificanteSIR.setNumeroRegistro(justificante.getNumeroRegistro());
		justificanteSIR.setTsAnexo(justificante.getTsAnexo());
		
		return justificanteSIR;
	}
	
	public Justificante JustificanteSIRToJustificanteFlexia(es.altia.flexia.sir.soap.generated.Justificante justificanteSIR){
		
		Justificante justificante = new Justificante();
		justificante.setCdTpDoc(justificanteSIR.getCdTpDoc());
		justificante.setCdValidez(justificanteSIR.getCdValidez());
		justificante.setCodIntercambio(justificanteSIR.getCdIntercambio());
		justificante.setContenido(justificanteSIR.getContenido());
		justificante.setCsv(justificanteSIR.getCsv());
		justificante.setDsCertificado(justificanteSIR.getDsCertificado());
		justificante.setDsFirma(justificanteSIR.getDsFirma());
		justificante.setDsHash(justificanteSIR.getDsHash());
		justificante.setDsTpMime(justificanteSIR.getDsTpMime());
		justificante.setDsValCertificado(justificanteSIR.getDsValCerficado());
		justificante.setFechaHoraPresentacion(justificanteSIR.getFechaHoraPresentacion());
		justificante.setFechaHoraRegistro(justificanteSIR.getFechaHoraRegistro());
		justificante.setHash(justificanteSIR.getHash());
		justificante.setIdFichero(justificanteSIR.getIdFichero());
		justificante.setIsFirmado(justificanteSIR.isIsFirmado());
		justificante.setNombre(justificanteSIR.getNombre());
		justificante.setNumeroRegistro(justificanteSIR.getNumeroRegistro());
		justificante.setTsAnexo(justificanteSIR.getTsAnexo());
		
		return justificante;
		
	}
}
