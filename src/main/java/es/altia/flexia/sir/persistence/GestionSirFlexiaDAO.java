package es.altia.flexia.sir.persistence;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.model.TipoDocumentoAnexo;
import es.altia.flexia.sir.model.TipoDocumentoIdentificacion;
import es.altia.flexia.sir.model.TipoRegistro;
import es.altia.flexia.sir.model.TipoTransporte;
import es.altia.util.ParserUtils;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.RowResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class GestionSirFlexiaDAO {

	private static volatile GestionSirFlexiaDAO instance = null;
	private final Logger LOGGER = Logger.getLogger(GestionSirFlexiaDAO.class);

	/**
	 * Factory method para el <code>Singleton</code>.
	 *
	 * @return La unica instancia de GestionSirFlexiaDAO
	 */
	public static GestionSirFlexiaDAO getInstance() {
		GestionSirFlexiaDAO dao = GestionSirFlexiaDAO.instance;
		//Si no hay una instancia de esta clase tenemos que crear una.
		if (dao == null) {
			// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized (GestionSirFlexiaDAO.class) {
				dao = GestionSirFlexiaDAO.instance;
				if (dao == null) {
					GestionSirFlexiaDAO.instance = dao = new GestionSirFlexiaDAO();
				}
			}
		}
		return dao;
	}

	private GestionSirFlexiaDAO() {

	}

	/**
	 * Recupera de la base de datos el registro asociado con los parámetros de entrada a partir de la tabla R_RES, .
	 *
	 * @param uor RES_UOR
	 * @param tipoES RES_TIP
	 * @param ejercicio RES_EJE
	 * @param numeroRegistro RES_NUM
	 * @param departamento RES_DEP
	 * @param conexion
	 * @return
	 * @throws GestionSirException Si no hay coincidencias en la DB.
	 * @throws SQLException
	 */
	public Asiento buscarAsiento(String uor, String tipoES, int ejercicio, int numeroRegistro,
			int departamento, Connection conexion) throws GestionSirException, SQLException {

		SqlBuilder sql = new SqlBuilder()
				.select()
				.from("R_RES")
				// Where
				.whereEqualsParametrizado("RES_UOR")
				.andEqualsParametrizado("RES_TIP")
				.andEqualsParametrizado("RES_EJE")
				.andEqualsParametrizado("RES_NUM")
				.andEqualsParametrizado("RES_DEP");
		QueryResult resultado;

		try {
			SqlExecuter consulta = new SqlExecuter(sql);
			consulta.setValues(uor, tipoES, ejercicio, numeroRegistro, departamento).logSqlDebug(LOGGER);
			resultado = consulta.executeQuery(conexion);
			if (resultado.next()) {
				Asiento asiento = rowResultAAsiento(resultado.getRow());
				return asiento;
			} else {
				throw new GestionSirException(CodigoMensajeSir.ASIENTO_NO_ENCONTRADO.getCodigo(),
						"No ha habido coincidencias para la búsqueda de asiento", String.format(
								"Asiento{uor: %s, tipoES: %s, ejercicio: %d, numeroRegistro: %d, departamento: %d}",
								uor, tipoES, ejercicio, numeroRegistro, departamento));
			}

		} catch (SQLException ex) {
			LOGGER.error("Error al recuperar el registro: " + ex);
			throw ex;
		}
	}

	/**
	 * Recupera de la base de datos los terceros asociados a un registro. Acepta como parámetro la PK de un registro de
	 * la tabla R_RES.
	 *
	 * @param uor
	 * @param tipoES
	 * @param ejercicio
	 * @param numeroRegistro
	 * @param departamento
	 * @param con
	 * @return
	 * @throws GestionSirException Si no hay coincidencias en la DB.
	 * @throws java.sql.SQLException
	 */
	public List<Interesado> buscarInteresados(String uor, String tipoES, int ejercicio, int numeroRegistro,
			int departamento, Connection con) throws GestionSirException, SQLException {

		SqlBuilder sql = new SqlBuilder()
				.select("T_TER.*", "T_DNN.*", "T_TVI.*", "T_VIA.*")
				.from("R_EXT")
				// Join tabla terceros
				.innerEquiJoin("T_TER", "EXT_TER", "TER_COD")
				// Join tabla domicilios para recuperar domicilio principal
				.innerEquiJoin("T_DNN", "TER_DOM", "DNN_DOM")
				// Join tabla de tipo de vías, que incluye descripción y abreviatura
				.innerEquiJoin("T_TVI", "DNN_TVI", "TVI_COD")
				// Join tabla vías para recuperar más detalles de la dirección
				.innerEquiJoin("T_VIA", "DNN_PAI", "VIA_PAI")
				.andEquals("DNN_PRV", "VIA_PRV")
				.andEquals("DNN_MUN", "VIA_MUN")
				.andEquals("DNN_VIA", "VIA_COD")
				// Where
				.whereEqualsParametrizado("EXT_UOR")
				.andEqualsParametrizado("EXT_TIP")
				.andEqualsParametrizado("EXT_EJE")
				.andEqualsParametrizado("EXT_NUM")
				.andEqualsParametrizado("EXT_DEP");

		QueryResult resultado;

		try {
			SqlExecuter consulta = new SqlExecuter(sql);
			consulta.setValues(uor, tipoES, ejercicio, numeroRegistro, departamento).logSqlDebug(LOGGER);
			resultado = consulta.executeQuery(con);

			List<Interesado> terceros = queryResultAListInteresado(resultado);

			return terceros;

		} catch (SQLException ex) {
			LOGGER.error("Error al recuperar el registro: " + ex);
			throw ex;
		}
	}

	/**
	 * Recupera de la base de datos los anexos asociados a un registro. Acepta como parámetro la PK de un registro de la
	 * tabla de R_RES.
	 *
	 * @param uor
	 * @param tipoES
	 * @param ejercicio
	 * @param numeroRegistro
	 * @param departamento
	 * @param con
	 * @return
	 * @throws GestionSirException
	 * @throws java.sql.SQLException
	 */
	public List<Anexo> buscarAnexos(String uor, String tipoES, int ejercicio, int numeroRegistro,
			int departamento, Connection con) throws GestionSirException, SQLException {

		SqlBuilder sql = new SqlBuilder()
				.select()
				.from("R_RED")
				// Where
				.whereEqualsParametrizado("RED_UOR")
				.andEqualsParametrizado("RED_TIP")
				.andEqualsParametrizado("RED_EJE")
				.andEqualsParametrizado("RED_NUM")
				.andEqualsParametrizado("RED_DEP");

		QueryResult resultado;

		try {
			SqlExecuter consulta = new SqlExecuter(sql);
			consulta.setValues(uor, tipoES, ejercicio, numeroRegistro, departamento).logSqlDebug(LOGGER);
			resultado = consulta.executeQuery(con);

			List<Anexo> anexos = queryResultAListAnexo(resultado);

			return anexos;

		} catch (SQLException ex) {
			LOGGER.error("Error al recuperar el registro: " + ex);
			throw ex;
		}
	}

	/**
	 * Recupera de la base de datos los asientos que tengan el estado marcado como pendiente.
	 *
	 * @param conexion
	 * @return
	 * @throws SQLException
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public List<Asiento> buscarAsientosParaEnviar(Connection conexion) throws SQLException, GestionSirException {
		// El campo RES_MOD con valor a 1 indica que un registro tiene de destino ser enviado a otra oficina
		final int REGISTROS_PARA_OTRA_OFICINA = 1;

		SqlBuilder sql = new SqlBuilder()
				.select()
				.from("R_RES")
				// Where
				.whereEqualsParametrizado("RES_MOD")
				.andEqualsParametrizado("RES_EJE")
				// Order by (para ir mirando los previos) 
				.orderBy("RES_UOR", "RES_TIP", "RES_EJE", "RES_NUM", "RES_DEP");

		QueryResult resultado;

		int ejercicioActual = Calendar.getInstance().get(Calendar.YEAR);
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(REGISTROS_PARA_OTRA_OFICINA, ejercicioActual).logSqlDebug(LOGGER);
		resultado = consulta.executeQuery(conexion);

		List<Asiento> asientos = new ArrayList<Asiento>();
		// Parsear asientos y recuperar Interesados y Anexos para cada uno de ellos
		while (resultado.next()) {
			Asiento asiento = rowResultAAsiento(resultado.getRow());
			int numeroRegistro = ParserUtils.parsear(asiento.getNumRegistroEntrada(), 0);
			String tipoRegistro = resultado.getString("RES_TIP");
			asiento.setAnexos(buscarAnexos(asiento.getCodEntidadRegistralInicio(), tipoRegistro, asiento.getEjercicio(),
					numeroRegistro, asiento.getDepartamento(), conexion));
			asiento.setInteresados(buscarInteresados(asiento.getCodEntidadRegistralInicio(), tipoRegistro,
					asiento.getEjercicio(), numeroRegistro, asiento.getDepartamento(), conexion));
			asientos.add(asiento);
		}

		return asientos;
	}

	/**
	 * Actualiza los datos de un {@link RegistroValueObject} en base de datos.
	 *
	 * @param registro
	 * @param con
	 */
	public void actualizarRegistro(RegistroValueObject registro, Connection con) {
		SqlBuilder sql = new SqlBuilder()
				.updateParametrizado("R_RES", "RES_UCO", "RES_FEC", "RES_UCD", "ASUNTO", "RES_TTR", "RES_NTR",
						"RES_MOD", "RES_TIP");
		try {
			SqlExecuter consulta = new SqlExecuter(sql);
			consulta.addValues(
					registro.getIdUndRegOrigen(),
					registro.getFecEntrada(),
					registro.getIdUndRegDest(),
					registro.getCodAsunto(),
					// registro.getNumExpediente(), TODO Dónde está en BD?
					registro.getIdTransporte(),
					registro.getNumTransporte(),
					registro.getTipoAnot(),
					registro.getTipoReg()
			).logSqlDebug(LOGGER);
			int resultado = consulta.executeUpdate(con);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("%s registros han sido modificados", resultado));
			}

		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
		}
	}

	/**
	 * Actualiza los datos de un {@link Tercero} en base de datos, incluyendo los datos del {@link TercerosValueObject}
	 * y del {@link DomicilioSimpleValueObject} contenidos en éste.
	 *
	 * @param terceros
	 * @param con
	 */
	public void actualizarTerceros(List<Interesado> terceros, Connection con) throws SQLException {
		SqlBuilder sql = new SqlBuilder()
				.updateParametrizado("T_TER", "TER_TID", "TER_DOC", "TER_NOM", "TER_PA1", "TER_AP1", "TER_PA2",
						"TER_AP2", "TER_DCE", "TER_TLF")
				.whereEqualsParametrizado("TER_COD");
		SqlExecuter consulta = new SqlExecuter(sql);

//		for (Tercero tercero : terceros) {
//			try {
//				TercerosValueObject terceroVO = tercero.getTercero();
//				DomicilioSimpleValueObject domicilio = tercero.getDomicilio();
//				actualizarDomicilio(domicilio, con);
//				consulta.setValues(
//						terceroVO.getTipoDocumento(),
//						terceroVO.getNombre(),
//						terceroVO.getPartApellido1(),
//						terceroVO.getApellido1(),
//						terceroVO.getPartApellido2(),
//						terceroVO.getApellido2(),
//						terceroVO.getEmail(),
//						terceroVO.getTelefono(),
//						terceroVO.getCodTerceroOrigen()
//				).logSqlDebug(log);
//				int resultado = consulta.executeUpdate(con);
//
//				if (log.isDebugEnabled()) {
//					log.debug(String.format("%s terceros han sido modificados", resultado));
//				}
//			} catch (SQLException ex) {
//				log.error("Error al actualizar los terceros: " + ex);
//				throw ex;
//			}
//		}
	}

	/**
	 * Actualiza un {@link DomicilioSimpleValueObject} en base de datos.
	 *
	 * @param domicilio
	 * @param con
	 */
	public void actualizarDomicilio(DomicilioSimpleValueObject domicilio, Connection con) throws SQLException {
		SqlBuilder sql = new SqlBuilder()
				.updateParametrizado("T_DNN", "DNN_PAI", "DNN_PRV", "DNN_MUN", "DNN_DMC", "DNN_CPO")
				.whereEqualsParametrizado("DNN_DOM");
		try {
			SqlExecuter consulta = new SqlExecuter(sql);
			consulta.addValues(
					domicilio.getIdPais(),
					domicilio.getIdProvincia(),
					domicilio.getIdMunicipio(),
					domicilio.getDomicilio(),
					domicilio.getCodigoPostal(),
					domicilio.getIdDomicilio()
			).logSqlDebug(LOGGER);
			int resultado = consulta.executeUpdate(con);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("%s domicilios han sido modificados", resultado));
			}

		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
			throw ex;
		}
	}

	/* QueryResult y RowResult parsers */
	/**
	 * Parsea una fila de un QueryResult a un RegistroValueObject a partir de la tabla R_RES.
	 *
	 * @param resultado
	 * @return
	 */
	private Asiento rowResultAAsiento(RowResult resultado) {
		Asiento asiento = new Asiento();
//		asiento.setAppVersion(resultado.getString());										// TODO
		asiento.setCodAsunto(resultado.getString("ASUNTO"));
//		asiento.setCodDocFisica(resultado.getString());										// TODO
//		asiento.setCodEntidadRegistralDestino(resultado.getString());						// TODO
		asiento.setCodEntidadRegistralInicio(resultado.getString("RES_UOR"));
		asiento.setCodEntidadRegistralOrigen(resultado.getString("RES_UCO"));
//		asiento.setCodExterno(resultado.getString());										// TODO
//		asiento.setCodIntercambio(resultado.getString());									// TODO
//		asiento.setCodTipoAnotacion(resultado.getString());									// TODO
		asiento.setCodTipoRegistro(TipoRegistro.getEnum(resultado.getString("RES_TIP")));
		asiento.setCodTipoTransporte(TipoTransporte.getEnum(resultado.getString("RES_TTR")));
		asiento.setCodUnidadTramitadoraDestino(resultado.getString("RES_UCD"));
//		asiento.setCodUnidadTramitadoraOrigen(resultado.getString());						// TODO
//		asiento.setContactoUsuario(resultado.getString());									// TODO
		asiento.setDescripcionAsunto(resultado.getString("RES_ASU"));						// TODO
//		asiento.setDescripcionEntidadRegistralDestino(resultado.getString());				// TODO
//		asiento.setDescripcionEntidadRegistralInicio(resultado.getString());				// TODO
//		asiento.setDescripcionEntidadRegistralOrigen(resultado.getString());				// TODO
		asiento.setDescripcionTipoAnotacion(resultado.getString("RES_MOD"));
//		asiento.setDescripcionUnidadTramitadoraDestino(resultado.getString());				// TODO
//		asiento.setDescripcionUnidadTramitadoraOrigen(resultado.getString());				// TODO
//		asiento.setEstado(resultado.getString());											// TODO
//		asiento.setExpone(resultado.getString());											// TODO
		asiento.setFechaEntrada(resultado.get("RES_FEC", Date.class));
//		asiento.setNombreUsuario(resultado.getString());									// TODO
//		asiento.setNumExpediente(resultado.getString());									// TODO
		asiento.setNumRegistroEntrada(resultado.getString("RES_NUM"));
		asiento.setNumTransporte(resultado.getString("RES_NTR"));
//		asiento.setObservaciones(resultado.getString());									// TODO
//		asiento.setReferenciaExterna(resultado.getString());								// TODO
//		asiento.setResumen(resultado.getString());											// TODO
//		asiento.setSolicita(resultado.getString());											// TODO
//		asiento.setTimestampEntrada(resultado.getString());									// TODO
		// Atributos Flexia
		asiento.setEjercicio(ParserUtils.wrapperAPrimitivo(resultado.getInteger("RES_EJE"), 0));
		asiento.setDepartamento(ParserUtils.wrapperAPrimitivo(resultado.getInteger("RES_DEP"), 0));
		asiento.setNumRegistro(ParserUtils.wrapperAPrimitivo(resultado.getInteger("RES_NUM"), 0));
		asiento.setTipoRegistro(resultado.getString("RES_TIP"));
		asiento.setUnidadRegistro(ParserUtils.wrapperAPrimitivo(resultado.getInteger("RES_UOD"), 0));
		
		return asiento;
	}

	/**
	 * Parsea los resultados de un {@link QueryResult} a una lista de {@link Interesado interesados} a partir de las
	 * tablas T_TER, T_DNN, T_TVI y T_VIA.
	 *
	 * @param resultado
	 * @return
	 */
	private List<Interesado> queryResultAListInteresado(QueryResult resultado) {
		List<Interesado> interesados = new ArrayList<Interesado>();
		while (resultado.next()) {
			Interesado interesado = rowResultAInteresado(resultado.getRow());
			interesado.setDomicilio(rowResultADomicilio(resultado.getRow()));
			interesados.add(interesado);
		}
		return interesados;
	}

	/**
	 * Parsea un {@link RowResult} a un {@link Interesado} de la tabla T_TER.
	 *
	 * @param resultado
	 * @return
	 */
	private Interesado rowResultAInteresado(RowResult resultado) {
		Interesado interesado = new Interesado();
		// interesado.setCanalPreferenteComunicacion(resultado.getString());		// TODO
		interesado.setCorreoElectronico(resultado.getString("TER_DCE"));
		// interesado.setDireccionElectronicaHabilitada(resultado.getString());		// TODO
		interesado.setDocumentoIdentificacion(resultado.getString("TER_DOC"));
		interesado.setNombreRazonSocial(resultado.getString("TER_NOM"));
		// interesado.setObservaciones(resultado.getString());						// TODO
		interesado.setPrimerApellido(resultado.getString("TER_AP1"));
		interesado.setSegundoApellido(resultado.getString("TER_AP2"));
		interesado.setTelefono(resultado.getString("TER_TLF"));
		interesado.setTipoDocumentoIdentificacion(TipoDocumentoIdentificacion.getEnum(resultado.getString("TER_TID")));
		// Atributos Flexia
		interesado.setCodTercero(resultado.getInteger("TER_COD"));

		return interesado;
	}

	/**
	 * Parsea una fila de un QueryResult a un Domicilio a partir de una fila de las tablas T_DNN, T_TVI y T_VIA.
	 *
	 * @param resultado
	 * @return
	 */
	private Domicilio rowResultADomicilio(RowResult resultado) {
		Domicilio domicilio = new Domicilio();
		domicilio.setCodPostal(resultado.getString("DNN_CPO"));
		domicilio.setDireccion(resultado.getString("DNN_DMC"));
		domicilio.setMunicipio(resultado.getString("DNN_MUN"));
		domicilio.setPais(resultado.getString("DNN_PAI"));
		domicilio.setProvincia(resultado.getString("DNN_PRV"));
		// Atributos Flexia
		domicilio.setCodDnn(resultado.getString("DNN_DOM"));
		domicilio.setCodTipoVia(resultado.getString("TVI_COD"));
		domicilio.setCodVia(resultado.getString("VIA_COD"));

		return domicilio;
	}

	/**
	 * Parsea una fila de un QueryResult a una lista de {@link Anexo anexos} a partir de la tabla R_RED.
	 *
	 * @param resultado
	 * @return
	 * @throws SQLException
	 */
	private List<Anexo> queryResultAListAnexo(QueryResult resultado) throws SQLException {
		List<Anexo> anexos = new ArrayList<Anexo>();
		while (resultado.next()) {
			Anexo anexo = rowResultAAnexo(resultado.getRow());
			anexos.add(anexo);
		}

		return anexos;
	}

	/**
	 * Parsea una {@link RowResult} a un {@link Anexo} leyendo de la tabla R_RED.
	 *
	 * @param resultado
	 * @return
	 * @throws SQLException
	 */
	private Anexo rowResultAAnexo(RowResult resultado) throws SQLException {
		Anexo anexo = new Anexo();
		// anexo.setCertificado(resultado.getString());						// TODO
		anexo.setContenido(resultado.getBlobBytes("RED_DOC"));
		// anexo.setCsv(resultado.getString());								// TODO
		// anexo.setFirmaDocumento(resultado.getString());					// TODO
		// anexo.setHash(resultado.getString());							// TODO
		// anexo.setIdentificadorDocumentoFirmado(resultado.getString());	// TODO
		// anexo.setIdentificadorFichero(resultado.getString());			// TODO
		anexo.setNombreFichero(resultado.getString("RED_NOM_DOC"));
		// anexo.setObservaciones(resultado.getString());					// TODO
		anexo.setTimestamp(resultado.get("RED_FEC_DOC", Date.class));
		anexo.setTipoDocumento(TipoDocumentoAnexo.getEnum(resultado.getString("RED_TIP_DOC")));
		// anexo.setTipoMIME(resultado.getString());						// TODO
		// anexo.setValidacionOCSPCertificado(resultado.getString());		// TODO
		// anexo.setValidezDocumento(resultado.getString());				// TODO
		// Atributos Flexia
		anexo.setCodDocumento(ParserUtils.parsear(resultado.getString("RED_DOC_ID"), 0));

		return anexo;
	}

	public void actualizarEstadoRegistro(SqlExecuter executer, Connection conexion) throws SQLException {
		try {
			int resultado = executer.executeUpdate(conexion);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("%s domicilios han sido modificados", resultado));
			}
		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
			throw(ex);
		}
	}
	
	public QueryResult recuperarCodJustificanteByIntercambio(SqlExecuter sqlExecuter, Connection conexion) throws SQLException {
		try {
			return sqlExecuter.executeQuery(conexion);
		} catch (SQLException ex) {
			LOGGER.error("Error al recuperar el id del justificante: " + ex);
			throw (ex);
		}
	}
}
