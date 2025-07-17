package es.altia.flexia.sir.soap;

import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Intercambio;
import es.altia.flexia.sir.model.Justificante;
import es.altia.flexia.sir.model.RespuestaAsiento;
import es.altia.flexia.sir.model.TipoRespuesta;
import es.altia.flexia.sir.soap.generated.FlexiaInternoServicePortService;
import es.altia.flexia.sir.soap.generated.ReencolarRequest;
import es.altia.flexia.sir.soap.generated.ReencolarResponse;
import es.altia.flexia.sir.soap.generated.RegistrarEnviarRequest;
import es.altia.flexia.sir.soap.generated.RegistrarEnviarResponse;
import es.altia.flexia.sir.soap.generated.ResponderRecibidoRequest;
import es.altia.flexia.sir.soap.generated.ResponderRecibidoResponse;
import es.altia.flexia.sir.soap.mapper.AnexoMapper;
import es.altia.flexia.sir.soap.mapper.AsientoMapper;
import es.altia.flexia.sir.soap.mapper.JustificanteMapper;
import es.altia.flexia.sir.soap.mapper.RespuestaAsientoMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.viafirma.cliente.exception.CodigoError;

public class GestionSirClienteSOAP {

	private static volatile GestionSirClienteSOAP instance = null;
	private final Logger LOGGER = Logger.getLogger(GestionSirClienteSOAP.class);
	private FlexiaInternoServicePortService service = new FlexiaInternoServicePortService();
	
	private GestionSirClienteSOAP() {
	}
	
	/**
	 * Factory method para el <code>Singleton</code>.
	 *
	 * @return La unica instancia de GestionSirClienteSOAP
	 */
	public static GestionSirClienteSOAP getInstance() {
		GestionSirClienteSOAP instance = GestionSirClienteSOAP.instance;
		//Si no hay una instancia de esta clase tenemos que crear una.
		if (instance == null) {
			// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized (GestionSirClienteSOAP.class) {
				instance = GestionSirClienteSOAP.instance;
				if (instance == null) {
					GestionSirClienteSOAP.instance = instance = new GestionSirClienteSOAP();
				}
			}
		}
		return instance;
	}

	/**
	 * Envia una respuesta de confirmacion al CIR conforme el {@link Intercambio} se acepta. Al aceptar el intercambio, 
	 * se entiende que este y sus datos pertenecen al usuario de la aplicacion, por lo que se guardan en completitud.
	 *
	 * @param intercambio {@link Intercambio} a confirmar.
	 * @param codEntidad Codigo de la entidad que confirma el intercambio.
	 */
	public Intercambio aceptarIntercambio(Intercambio intercambio, String codEntidad) {
		RespuestaAsiento respuesta = new RespuestaAsiento(intercambio.getAsiento(), TipoRespuesta.CONFIRMACION, 
				codEntidad);
		ResponderRecibidoRequest request = new ResponderRecibidoRequest();
		RespuestaAsientoMapper respuestaAsientomapper = new RespuestaAsientoMapper();
		AnexoMapper anexoMapper = new AnexoMapper();
		es.altia.flexia.sir.soap.generated.RespuestaAsiento respuestaAsientoSOAP = respuestaAsientomapper.RespuestaAsientoFlexiaToRespuestaAsientoSIR(respuesta);
		// respuestaAsientoSOAP.setNuRgEntradaDestino(Integer.toString(intercambio.getAsiento().getNumRegistro())); Como obtener Num Reg? Guardar primero?
		
		GregorianCalendar c = new GregorianCalendar();
		Date fecha = new Date();
		c.setTime(fecha);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		
		} catch (DatatypeConfigurationException ex) {
		
			java.util.logging.Logger.getLogger(GestionSirClienteSOAP.class.getName()).log(Level.SEVERE, null, ex);
		
		}
		respuestaAsientoSOAP.setFeEntradaDestino(xmlGregorianCalendar);
		request.setRespuesta(respuestaAsientoSOAP);
		ResponderRecibidoResponse response = service.getFlexiaInternoServicePortSoap11().responderRecibido(request);
		
		List<Anexo> anexoList = new ArrayList<Anexo>();
		for(es.altia.flexia.sir.soap.generated.Anexo anexo : response.getAnexos()){
			anexoList.add(anexoMapper.AnexoSIRToAnexoFlexia(anexo));
		}
		intercambio.getAsiento().setAnexos(anexoList);
		
		return intercambio;
	}

	/**
	 * Envia una respuesta de rechazo al CIR conforme el {@link Intercambio} se acepta.
	 *
	 * @param intercambio {@link Intercambio} a rechazar.
	 * @param codEntidad Codigo de la entidad que confirma el intercambio.
	 * @param motivo Razon de rechazo del {@link Intercambio}.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */

	public void rechazarRecibido(Intercambio intercambio, String codEntidad, String motivo) throws GestionSirException {

		RespuestaAsiento respuesta = new RespuestaAsiento(intercambio.getAsiento(), TipoRespuesta.RECHAZO, codEntidad, motivo);
		ResponderRecibidoRequest request = new ResponderRecibidoRequest();
		RespuestaAsientoMapper respuestaAsientomapper = new RespuestaAsientoMapper();
		es.altia.flexia.sir.soap.generated.RespuestaAsiento respuestaAsientoSOAP = respuestaAsientomapper.RespuestaAsientoFlexiaToRespuestaAsientoSIR(respuesta);
		// respuestaAsientoSOAP.setNuRgEntradaDestino(Integer.toString(intercambio.getAsiento().getNumRegistro())); Como obtener Num Reg? Si se rechaza no llegamos a crearlo en ningun momento
		GregorianCalendar c = new GregorianCalendar();
		Date fecha = new Date();
		c.setTime(fecha);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

		} catch (DatatypeConfigurationException ex) {

			java.util.logging.Logger.getLogger(GestionSirClienteSOAP.class.getName()).log(Level.SEVERE, null, ex);

		}
		respuestaAsientoSOAP.setFeEntradaDestino(xmlGregorianCalendar);
		request.setRespuesta(respuestaAsientoSOAP);
		ResponderRecibidoResponse response = service.getFlexiaInternoServicePortSoap11().responderRecibido(request);
	}

	public void reencolar(String cdEnRgProcesa, List<String> cdIntercambio) {
		
		ReencolarRequest request = new ReencolarRequest();
		ReencolarResponse response = new ReencolarResponse();
		
		request.getCodIntercambio().addAll(cdIntercambio);
		request.setCdEnRgProcesa(cdEnRgProcesa);
		
		response = service.getFlexiaInternoServicePortSoap11().reencolar(request);
		
	}

	public void reenviarRecibido(RespuestaAsiento respuesta) {
		// Es posible que este metodo no se implemente
		throw new UnsupportedOperationException("The method is not implemented yet.");
	}

	
	public Justificante enviarAsientosPendientes(Asiento asiento){
		
		AsientoMapper asientoMapper = new AsientoMapper();
		JustificanteMapper justificanteMapper = new JustificanteMapper();
		
		es.altia.flexia.sir.soap.generated.Asiento asientoSIR = asientoMapper.AsientoFlexiaToAsientoSIR(asiento);
		RegistrarEnviarRequest request = new RegistrarEnviarRequest();
		request.setAsiento(asientoSIR);
		RegistrarEnviarResponse response = service.getFlexiaInternoServicePortSoap11().registrarEnviar(request);
		es.altia.flexia.sir.soap.generated.Justificante justificanteSIR = response.getJustificante();
		Justificante justificante = justificanteMapper.JustificanteSIRToJustificanteFlexia(justificanteSIR);
		
		return justificante;
		
		
				
	}
}
