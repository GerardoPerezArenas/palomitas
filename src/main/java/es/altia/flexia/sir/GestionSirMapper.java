package es.altia.flexia.sir;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.eni.conversoreni.StringUtils;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.model.TipoDocumentoIdentificacion;
import es.altia.util.ParserUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * Conversor de clases propias de Flexia y aquellas creadas para el soporte del SIR en Flexia.
 */
public class GestionSirMapper {

	private static volatile GestionSirMapper instance = null;
	public static final String FORMATO_FECHA_REGISTRO = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_FECHA_DOCUMENTO = "dd/MM/yyyy";
	private final Logger LOGGER = Logger.getLogger(GestionSirMapper.class);

	/**
	 * Factory method para el <code>Singleton</code>.
	 *
	 * @return La unica instancia de AsientoMapper
	 */
	public static GestionSirMapper getInstance() {
		GestionSirMapper mapper = GestionSirMapper.instance;
		//Si no hay una instancia de esta clase tenemos que crear una.
		if (mapper == null) {
			// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized (GestionSirMapper.class) {
				mapper = GestionSirMapper.instance;
				if (mapper == null) {
					GestionSirMapper.instance = mapper = new GestionSirMapper();
				}
			}
		}
		return mapper;
	}

	private GestionSirMapper() {
	}

	/**
	 * Convierte un objeto {@link Asiento} con sus respectivos {@link Anexo anexos}, {@link Interesado interesados} y
	 * {@link Domicilio domicilios} en un {@link RegistroValueObject}, asociado respectivamente a otros
	 * {@link RegistroValueObject registros} que en Flexia tambien cumplen la funcion de documentos anexos de un
	 * registro y {@link TercerosValueObject terceros} con un {@link DomicilioSimpleValueObject}.
	 *
	 * @param asiento Asiento con formato SIR a transformar en un {@link RegistroValueObject}.
	 * @param idUsuario Identificador del usuario que da de alta el asiento.
	 * @param appCod Codigo de la aplicacion desde la que se da de alta el asiento.
	 * @return El {@link RegistroValueObject} generado.
	 */
	public RegistroValueObject convertir(Asiento asiento, int idUsuario, int appCod) {
		RegistroValueObject registro = new RegistroValueObject();
		// TODO Determinar los campos restantes

//		registro.set(asiento.getAppVersion());
//		registro.set(asiento.getCodAsiento());
//		registro.set(asiento.getCodExterno());
		registro.setCodAsunto(null);																	// R_RES.ASUNTO
		registro.setAsunto(null);																		// R_RES.ASUNTO 
//		registro.set(asiento.getCodDocFisica());
		registro.setIdUndRegDest(asiento.getCodEntidadRegistralDestino());								// R_RES.RES_UCD 
//		registro.set(asiento.getCodEntidadRegistralInicio());											// R_RES.RES_UOR 
		registro.setIdUndRegOrigen(asiento.getCodEntidadRegistralOrigen());								// R_RES.RES_UCO Código de la Entidad Registral de Origen.
//		registro.set(asiento.getCodIntercambio());														// NOT NULL
		registro.setTipoAnot(ParserUtils.parsear((asiento.getCodTipoAnotacion() != null)
				? asiento.getCodTipoAnotacion().getValor() : null, 0));									// R_RES.RES_MOD 
		registro.setTipoReg((asiento.getCodTipoRegistro() != null) 
				? asiento.getCodTipoRegistro().getValor() : "E");										// R_RES.RES_TIP TODO E por defecto
		registro.setIdTransporte(ParserUtils.parsear((asiento.getCodTipoTransporte() != null)
				? asiento.getCodTipoRegistro().getValor() : null, 0));									// R_RES.RES_TTR 
//		registro.set(asiento.getCodUnidadTramitadoraDestino());
//		registro.set(asiento.getCodUnidadTramitadoraOrigen());
//		registro.set(asiento.getContactoUsuario());
//		registro.set(asiento.getDescripcionEntidadRegistralDestino());
//		registro.set(asiento.getDescripcionEntidadRegistralInicio());
//		registro.set(asiento.getDescripcionEntidadRegistralOrigen());
//		registro.set(asiento.getDescripcionTipoAnotacion());
//		registro.set(asiento.getDescripcionUnidadTramitadoraDestino());
//		registro.set(asiento.getDescripcionUnidadTramitadoraOrigen());
//		registro.set(asiento.getEstado());
//		registro.set(asiento.getExpone());
		registro.setFecEntrada(formatearFechaRegistro(asiento.getFechaEntrada()));			// R_RES.RES_FEC "DD/MM/YYYY HH24:MI:SS" 
//		registro.set(asiento.getNombreUsuario());
//		registro.set(asiento.getNumExpediente());
		registro.setNumReg(ParserUtils.parsear(asiento.getNumRegistroEntrada(), 0));		// R_RES.RES_NUM 
		registro.setNumTransporte(asiento.getNumTransporte());								// R_RES.RES_NTR 
//		registro.set(asiento.getObservaciones());
//		registro.set(asiento.getReferenciaExterna());
//		registro.set(asiento.getResumen());
//		registro.set(asiento.getSolicita());
//		registro.set(asiento.getTimestampEntrada());
		registro.setAnoReg(ParserUtils.wrapperAPrimitivo(asiento.getEjercicio(), 0));											// R_RES.RES_EJE
//		registro.set(asiento.getTipoRegistro());											// R_RES.RES_TIP Los valores posibles son E/S, correspondiente a Entrada/Salida.
		registro.setIdentDepart(ParserUtils.wrapperAPrimitivo(asiento.getDepartamento(), 0));
		registro.setUsuarioQRegistra(Integer.toString(idUsuario));							// R_RES.RES_USU

		registro.setCodInter(ParserUtils.wrapperAPrimitivo(asiento.getInteresados().get(0).getCodTercero(), 0));

		// TODO registro.setCodOficinaRegistro(asiento.get);
		// TODO registro.setCodProcedimiento(asiento.get);
		// TODO registro.setDptoUsuarioQRegistra(asiento.get);
		// TODO registro.setDomicInter(asiento.get);
		// TODO registro.setEstAnotacion(asiento.get);
		registro.setHayBuzon(false); // Este registro no viene del buzon
		// TODO registro.setIdOrganizacion(asiento.get);
		// TODO registro.setListaCodDomicilio(asiento.get);
		// TODO registro.setListaCodTercero(asiento.get);
		// TODO registro.setListaVersionTercero(asiento.get);
		// TODO registro.setListaRol(asiento.get);
		// TODO registro.setNumModInfInt(asiento.get);
		// TODO registro.setObservaciones(asiento.getObservaciones());
		// TODO registro.setRespOpcion(asiento.get);
		// TODO registro.setUnidadOrgan(asiento.get);
		// TODO registro.setUnidOrgUsuarioQRegistra(asiento.get);

		// TODO Convertir Anexos, Interesados y Domicilios
		return registro;
	}

	/**
	 * Convierte un {@link Anexo} a un {@link RegistroValueObject}.
	 *
	 * @param anexo Anexo con formato SIR a transformar en un {@link RegistroValueObject}.
	 * @param entregado Si el documento ha sido entregado o no.
	 * @param estadoDocumentoRegistro Estado del documento. Puede ser nuevo, eliminado, grabado, vacio o modificado.
	 * @param rutaDocumentoRegistroDisco
	 * @return El {@link RegistroValueObject} generado.
	 */
	public RegistroValueObject convertir(Anexo anexo, boolean entregado, int estadoDocumentoRegistro,
			String rutaDocumentoRegistroDisco) {
		RegistroValueObject documentoRegistro = new RegistroValueObject();

		documentoRegistro.setDoc(anexo.getContenido());
//		documentoRegistro.set(anexo.getCsv());
//		documentoRegistro.set(anexo.getFirmaDocumento());
//		documentoRegistro.set(anexo.getHash());
//		documentoRegistro.set(anexo.getIdentificadorDocumentoFirmado());
//		documentoRegistro.set(anexo.getIdentificadorFichero());					TODO (R_RED.RED_DOC_ID)
		documentoRegistro.setObservaciones(anexo.getObservaciones());
//		documentoRegistro.set(anexo.getValidacionOCSPCertificado());
//		documentoRegistro.set(anexo.getValidezDocumento());
//		documentoRegistro.set(anexo.getCodDepartamento());						TODO (R_RED.RED_DEP)
//		documentoRegistro.set(anexo.getCodigoUorRegistro());					TODO (R_RED.RED_UOR)
//		documentoRegistro.set(anexo.getEjercicio());							TODO (R_RED.RED_EJE)
//		documentoRegistro.setNumReg(anexo.getNumeroAnotacion());
//		documentoRegistro.set(anexo.getTipoEntrada());							TODO (R_RED.RED_)

		documentoRegistro.setNombreDoc(anexo.getNombreFichero());
		documentoRegistro.setTipoDoc(anexo.getTipoDocumento().getValor());
		documentoRegistro.setFechaDoc(formatearFecha(anexo.getTimestamp(), FORMATO_FECHA_DOCUMENTO));
		documentoRegistro.setEntregado((entregado) ? "S" : "N");
		documentoRegistro.setEstadoDocumentoRegistro(estadoDocumentoRegistro); // ConstantesDatos.ESTADO_DOCUMENTO_X
		documentoRegistro.setRutaDocumentoRegistroDisco(rutaDocumentoRegistroDisco); // TODO Ver de donde sale
		return documentoRegistro;
	}

	/**
	 * Crea una instancia de {@link TercerosValueObject} a partir de un {@link Interesado}. El {@link Interesado
	 * requiere tener el {@link Domicilio} inicializado.
	 *
	 * @param interesado Interesado con formato SIR que se usa para la conversion a {@link TercerosValueObject}.
	 * @param idUsuario Identificador del usuario que da de alta al tercero.
	 * @param appCod Codigo de la aplicacion desde la que se da de alta al tercero.
	 * @param esAlta Si la situacion del tercero es una alta. Si es asi, el campo {@link TercerosValueObject#situacion}
	 * sera 'A'. En caso contrario, sera 'B'.
	 * @return El tercero generado.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public TercerosValueObject convertir(Interesado interesado, int idUsuario, int appCod, boolean esAlta) 
			throws GestionSirException {
		// TODO Es necesario cambiar cosas como el pais acorde a los codigos usados en Flexia
		TercerosValueObject tercero = new TercerosValueObject();
//		tercero.set(interesado.getCanalPreferenteComunicacion());
		tercero.setEmail(interesado.getCorreoElectronico());
		tercero.setDomPrincipal(interesado.getDomicilio().getCodDnn());
//		tercero.set(interesado.getDireccionElectronicaHabilitada());
		tercero.setDocumento(interesado.getDocumentoIdentificacion());
		tercero.setNombre(interesado.getNombreRazonSocial());
//		tercero.set(interesado.getObservaciones());
		tercero.setTelefono(interesado.getTelefono());
		tercero.setTipoDocumento(convertirTipoDocumentoIdentificacion(interesado.getTipoDocumentoIdentificacion()));
		tercero.setApellido1(interesado.getPrimerApellido());
		tercero.setApellido2(interesado.getSegundoApellido());
		tercero.setIdentificador((interesado.getCodTercero() != null) ? Integer.toString(interesado.getCodTercero()) : null);
		tercero.setModuloAlta(Integer.toString(appCod));
		tercero.setNombreCompleto(FormateadorTercero.getDescTercero(tercero.getNombre(), tercero.getPartApellido1(),
				tercero.getApellido1(), tercero.getPartApellido2(), tercero.getApellido2(), false));
		tercero.setSituacion((esAlta) ? 'A' : 'B');
		tercero.setUsuarioAlta(Integer.toString(idUsuario));
		tercero.setCodRol(-99); // Rol creado siempre en los scripts
		Vector<DomicilioSimpleValueObject> domicilios = new Vector<DomicilioSimpleValueObject>();
		DomicilioSimpleValueObject domicilio = convertir(interesado.getDomicilio());
		domicilios.add(domicilio);
		tercero.setDomicilios(domicilios);

		return tercero;
	}

	/**
	 * Convierte una lista de {@link Interesado interesados} en {@link TercerosValueObject terceros}.
	 *
	 * @param interesados Interesados con formato SIR que se usa para la conversion a {@link TercerosValueObject}.
	 * @param idUsuario Identificador del usuario que da de alta al tercero.
	 * @param appCod Codigo de la aplicacion desde la que se da de alta al tercero.
	 * @param esAlta Si la situacion del tercero es una alta. Si es asi, el campo {@link TercerosValueObject#situacion}
	 * sera 'A'. En caso contrario, sera 'B'.
	 * @return Los terceros generados.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public List<TercerosValueObject> convertir(List<Interesado> interesados, int idUsuario, int appCod, boolean esAlta) 
			throws GestionSirException {
		List<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
		for (Interesado interesado : interesados) {
			terceros.add(convertir(interesado, idUsuario, appCod, esAlta));
		}
		return terceros;
	}

	/**
	 * Convierte un {@link Domicilio} en un {@link DomicilioSimpleValueObject}.
	 *
	 * @param domicilio {@link Domicilio} a convertir.
	 * @return El {@link DomicilioSimpleValueObject} generado.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public DomicilioSimpleValueObject convertir(Domicilio domicilio) throws GestionSirException {
		DomicilioSimpleValueObject domicilioVO = new DomicilioSimpleValueObject();
		domicilioVO.setCodigoPostal(domicilio.getCodPostal());						// TODO Codigo postal deberia ser igual
//		domicilioVO.set(domicilio.getDireccion());									// TODO Ver la conversion de los attrs a direccion
		domicilioVO.setIdMunicipio(convertirMunicipio(domicilio.getMunicipio()));	// TODO Ver la conversion de los codigos
		domicilioVO.setIdPais(convertirPais(domicilio.getPais()));					// TODO Ver la conversion de los codigos
		domicilioVO.setIdProvincia(convertirProvincia(domicilio.getProvincia()));	// TODO Ver la conversion de los codigos
//		domicilioVO.set(domicilio.getDescVia());
		domicilioVO.setDomicilio(domicilio.getCodDnn());
		domicilioVO.setIdTipoVia(domicilio.getCodTipoVia());
		domicilioVO.setCodigoVia(domicilio.getCodVia());
		// TODO Pais, municipio y provincia tienen campos de tipo idAtributo y atributo, hay que ver cuales se usan
		domicilioVO.setEnPadron("0");												// NOT NULL, Valor obsoleto.
		domicilioVO.setNormalizado("2");											// TODO NOT NULL Estoy poniendo 2 porque parece el numero necesario para insertar un nuevo domicilio

		return domicilioVO;
	}

	/**
	 * Crea un objeto {@link Date} a partir de un {@link String}.
	 *
	 * @param fecha
	 * @return Devuelve la fecha parseada. Puede devolver null en caso de error o si el parametro es vacio o null.
	 */
	public Date formatearFecha(String fecha) {
		Date resultado = null;
		if (StringUtils.isNotNullOrEmpty(fecha)) {
			try {
				SimpleDateFormat formateador = new SimpleDateFormat(FORMATO_FECHA_REGISTRO);
				resultado = formateador.parse(fecha);
			} catch (ParseException ex) {
				LOGGER.debug("Ha habido un error en el parseo de la fecha, devolviendo null");
			}
		}
		return resultado;
	}

	/**
	 * Crea un objeto {@link String} a partir de un {@link Date}.
	 *
	 * @param date
	 * @return Devuelve la fecha parseada. Puede devolver null en caso de que el parametro sea null.
	 */
	public String formatearFechaRegistro(Date date) {
		String resultado = null;
		if (date != null) {
			SimpleDateFormat formateador = new SimpleDateFormat(FORMATO_FECHA_REGISTRO);
			resultado = formateador.format(date);
		}
		return resultado;
	}

	/**
	 * Crea un objeto {@link Date} a partir de un {@link String}.
	 *
	 * @param fecha Representacion de la fecha a parsear en como {@link String}.
	 * @param formato Formato a usar para el parseo.
	 * @return Devuelve la fecha parseada. Puede devolver null en caso de error o si el parametro es vacio o null.
	 */
	public Date formatearFecha(String fecha, String formato) {
		Date resultado = null;
		if (StringUtils.isNotNullOrEmpty(fecha)) {
			try {
				SimpleDateFormat formateador = new SimpleDateFormat(formato);
				resultado = formateador.parse(fecha);
			} catch (ParseException ex) {
				LOGGER.debug(String.format(
						"Ha habido un error en el parseo de la fecha, devolviendo null [Fecha: %s, Formato: %s]",
						fecha, formato));
			}
		}
		return resultado;
	}

	/**
	 * Crea un objeto {@link String} a partir de un {@link Date}.
	 *
	 * @param date Fecha a convertir en {@link String}.
	 * @param formato Formato a usar para el parseo.
	 * @return Devuelve la fecha parseada. Puede devolver null en caso de que el parametro sea null.
	 */
	public String formatearFecha(Date date, String formato) {
		String resultado = null;
		if (date != null) {
			SimpleDateFormat formateador = new SimpleDateFormat(formato);
			resultado = formateador.format(date);
		}
		return resultado;
	}

	/**
	 * Devuelve el codigo de municipio de Flexia equivalente al codigo de municipio del SIR.
	 *
	 * @param municipioSir
	 * @return
	 */
	private String convertirMunicipio(String municipioSir) {
		return municipioSir; // TODO crear correspondencia de codigos si fuera necesario
	}

	/**
	 * Devuelve el codigo de provincia de Flexia equivalente al codigo de provincia del SIR.
	 *
	 * @param provinciaSIR
	 * @return
	 */
	private String convertirProvincia(String provinciaSIR) {
		return provinciaSIR; // TODO crear correspondencia de codigos si fuera necesario
	}

	/**
	 * Devuelve el codigo de pais de Flexia equivalente al codigo de pais del SIR.
	 *
	 * @param paisSir
	 * @return
	 */
	private String convertirPais(String paisSir) throws GestionSirException {
		paisSir = "0724"; // TODO BORRAR!
		if (StringUtils.isNotNullOrEmpty(paisSir)) {
			if (paisSir.equals("0724")) { // Codigo de España del SIR
				// En Flexia solo existe actualmente el codigo de pais de España (108).
				return "108";
			}
			throw new GestionSirException(CodigoMensajeSir.ERROR_CODIGO_PAIS_NO_EXISTENTE.getCodigo(), 
					"No existe una equivalencia para el pais de codigo %d", paisSir);
		}
		return null; // TODO crear correspondencia de codigos si fuera necesario
	}

	/**
	 * Devuelve el codigo de Flexia equivalente al tipo de documento de identificacion del SIR.
	 *
	 * @param tipoDocumento {@link TipoDocumentoIdentificacion} a convertir en codigo de Flexia.
	 * @return Codigo de Flexia.
	 */
	private String convertirTipoDocumentoIdentificacion(TipoDocumentoIdentificacion tipoDocumento)
			throws GestionSirException {
		if (tipoDocumento != null) {
			switch (tipoDocumento) {
				case CIF:
					return "4";
				case CODIGO_ORIGEN:
					return "53";
				case DOCUMENTO_EXTRANJEROS:
					return "51";
				case NIF:
					return "1";
				case OTROS:
					return "52";
				case PASAPORTE:
					return "2";
				default:
					throw new GestionSirException(
							CodigoMensajeSir.ERROR_TIPO_DOCUMENTO_IDENTIFICACION_NO_EXISTENTE.getCodigo(),
							String.format("No existe correspondencia para el documento %s", tipoDocumento.getValor()),
							tipoDocumento.getValor());
			}
		} else {
			return "102";
		}
	}
}
