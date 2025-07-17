/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.sir.soap.mapper;

import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.model.TipoAnotacion;
import es.altia.flexia.sir.model.TipoDocumentacionFisica;
import es.altia.flexia.sir.model.TipoRegistro;
import es.altia.flexia.sir.model.TipoTransporte;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Juangc
 */
public class AsientoMapper {
	public AsientoMapper(){}
	
	/**
	 *
	 * @param asiento
	 * @return
	 */
	public es.altia.flexia.sir.soap.generated.Asiento AsientoFlexiaToAsientoSIR(Asiento asiento){
		es.altia.flexia.sir.soap.generated.Asiento asientoSir = new es.altia.flexia.sir.soap.generated.Asiento();
		
		asientoSir.setAppVersion(asiento.getAppVersion());
		asientoSir.setCodAsunto(asiento.getCodAsunto());
		asientoSir.setCodDocFisica(asiento.getCodDocFisica().getValor());
		asientoSir.setCodEntidadRegistralDestino(asiento.getCodEntidadRegistralDestino());
		asientoSir.setCodEntidadRegistralInicio(asiento.getCodEntidadRegistralInicio());
		asientoSir.setCodEntidadRegistralOrigen(asiento.getCodEntidadRegistralOrigen());
		asientoSir.setCodIntercambio(null);
		asientoSir.setCodTipoAnotacion(asiento.getCodTipoAnotacion().getValor());
		asientoSir.setCodTipoRegistro(asiento.getCodTipoRegistro().getValor());
		asientoSir.setCodTipoTransporte(asiento.getCodTipoTransporte().getValor());
		asientoSir.setCodUnidadTramitadoraDestino(asiento.getCodUnidadTramitadoraDestino());
		asientoSir.setCodUnidadTramitadoraOrigen(asiento.getCodUnidadTramitadoraOrigen());
		asientoSir.setContactoUsuario(asiento.getContactoUsuario());
		asientoSir.setDescripcionAsunto(asiento.getDescripcionAsunto());
		asientoSir.setDescripcionEntidadRegistralDestino(asiento.getDescripcionEntidadRegistralDestino());
		asientoSir.setDescripcionEntidadRegistralInicio(asiento.getDescripcionEntidadRegistralInicio());
		asientoSir.setDescripcionEntidadRegistralOrigen(asiento.getDescripcionEntidadRegistralOrigen());
		asientoSir.setDescripcionUnidadTramitadoraDestino(asiento.getDescripcionUnidadTramitadoraDestino());
		asientoSir.setDescripcionUnidadTramitadoraOrigen(asiento.getDescripcionUnidadTramitadoraOrigen());
		asientoSir.setEstado(null);
		asientoSir.setExpone(asiento.getExpone());
		asientoSir.setSolicita(asiento.getSolicita());
		
		XMLGregorianCalendar date2 = null;
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(asiento.getFechaEntrada());
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException ex) {
			Logger.getLogger(AsientoMapper.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		asientoSir.setFechaEntrada(date2);
		
		asientoSir.setNombreUsuario(asiento.getNombreUsuario());
		asientoSir.setNumExpediente(asiento.getNumExpediente());
		asientoSir.setNumTransporte(asiento.getNumTransporte());
		
		asientoSir.setNumeroRegistroEntrada(asiento.getNumRegistroEntrada());
		asientoSir.setObservaciones(asiento.getObservaciones());
		asientoSir.setReferenciaExterna(asiento.getReferenciaExterna());
		asientoSir.setTimestampEntrada(asiento.getTimestampEntrada());
		
		//TODO ANEXOS E INTERESADOS
		
		
		return asientoSir;
	}
	
	public Asiento AsientoSIRToAsientoFlexia (es.altia.flexia.sir.soap.generated.Asiento asiento){
		Asiento asientoFlexia = new Asiento();
		AnexoMapper anexoMapper = new AnexoMapper();
		InteresadoMapper interesadoMapper = new InteresadoMapper();
		
		List<Anexo> anexoList = new ArrayList<Anexo>();
		for (es.altia.flexia.sir.soap.generated.Anexo asientosir : asiento.getListAnexo()){
			anexoList.add(anexoMapper.AnexoSIRToAnexoFlexia(asientosir));
		}
		
		List<Interesado> interesadoList = new ArrayList<Interesado>();
		for(es.altia.flexia.sir.soap.generated.Interesado interesadosir : asiento.getListInteresado()){
			interesadoList.add(interesadoMapper.interesadoSIRToInteresadoFlexia(interesadosir));
		}
		
		asientoFlexia.setAnexos(anexoList);
		asientoFlexia.setAppVersion(asiento.getAppVersion());
		asientoFlexia.setCodAsunto(asiento.getCodAsunto());
		asientoFlexia.setCodDocFisica(TipoDocumentacionFisica.getEnum(asiento.getCodDocFisica()));
		asientoFlexia.setCodEntidadRegistralDestino(asiento.getCodEntidadRegistralDestino());
		asientoFlexia.setCodEntidadRegistralInicio(asiento.getCodEntidadRegistralInicio());
		asientoFlexia.setCodEntidadRegistralOrigen(asiento.getCodEntidadRegistralOrigen());
		asientoFlexia.setCodTipoAnotacion(TipoAnotacion.getEnum(asiento.getCodTipoAnotacion()));
		asientoFlexia.setCodTipoRegistro(TipoRegistro.getEnum(asiento.getCodTipoRegistro()));
		asientoFlexia.setCodTipoTransporte(TipoTransporte.getEnum(asiento.getCodTipoTransporte()));
		asientoFlexia.setCodUnidadTramitadoraDestino(asiento.getCodUnidadTramitadoraDestino());
		asientoFlexia.setCodUnidadTramitadoraOrigen(asiento.getCodUnidadTramitadoraOrigen());
		asientoFlexia.setContactoUsuario(asiento.getContactoUsuario());
		asientoFlexia.setDescripcionAsunto(asiento.getDescripcionAsunto());
		asientoFlexia.setDescripcionEntidadRegistralDestino(asiento.getDescripcionEntidadRegistralDestino());
		asientoFlexia.setDescripcionEntidadRegistralInicio(asiento.getDescripcionEntidadRegistralInicio());
		asientoFlexia.setDescripcionEntidadRegistralOrigen(asiento.getDescripcionEntidadRegistralOrigen());
		asientoFlexia.setDescripcionTipoAnotacion(asiento.getTipoAnotacion());
		asientoFlexia.setDescripcionUnidadTramitadoraDestino(asiento.getDescripcionUnidadTramitadoraDestino());
		asientoFlexia.setDescripcionUnidadTramitadoraOrigen(asiento.getDescripcionUnidadTramitadoraOrigen());
		asientoFlexia.setExpone(asiento.getExpone());
		asientoFlexia.setFechaEntrada(asiento.getFechaEntrada().toGregorianCalendar().getTime());
		asientoFlexia.setNombreUsuario(asiento.getNombreUsuario());
		asientoFlexia.setNumExpediente(asiento.getNumExpediente());
		asientoFlexia.setNumRegistroEntrada(asiento.getNumeroRegistroEntrada());
		asientoFlexia.setNumTransporte(asiento.getNumTransporte());
		asientoFlexia.setObservaciones(asiento.getObservaciones());
		asientoFlexia.setReferenciaExterna(asiento.getReferenciaExterna());
		asientoFlexia.setSolicita(asiento.getSolicita());
		asientoFlexia.setTimestampEntrada(asiento.getTimestampEntrada());
		asientoFlexia.setInteresados(interesadoList);
		
		return asientoFlexia;
	}
}
