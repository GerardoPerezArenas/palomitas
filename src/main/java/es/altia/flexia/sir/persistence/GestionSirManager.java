package es.altia.flexia.sir.persistence;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.sir.GestionSirMapper;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.EstadoAsiento;
import es.altia.flexia.sir.model.Intercambio;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.model.Justificante;
import es.altia.flexia.terceros.integracion.externa.excepciones.ErrorTransaccionalTerceroExternoException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;import org.apache.log4j.Logger;

public class GestionSirManager {

	private static volatile GestionSirManager instance = null;
	private final Logger LOGGER = Logger.getLogger(GestionSirManager.class);

	/**
	 * Factory method para el <code>Singleton</code>.
	 *
	 * @return La unica instancia de GestionSirManager
	 */
	public static GestionSirManager getInstance() {
		GestionSirManager manager = GestionSirManager.instance;
		//Si no hay una instancia de esta clase tenemos que crear una.
		if (manager == null) {
			// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este método
			synchronized (GestionSirManager.class) {
				manager = GestionSirManager.instance;
				if (manager == null) {
					GestionSirManager.instance = manager = new GestionSirManager();
				}
			}
		}
		return manager;
	}

	private GestionSirManager() {

	}

	/**
	 * Busca un {@link Asiento} en la base de datos, incluyendo los datos relacionados, es decir, sus
	 * {@link Interesado interesados} y {@link Anexo anexos}. La recuperacion del asiento es a traves de las tablas
	 * R_RES, R_EXT, T_TER, T_DNN, T_TVI, T_VIA y R_RED.
	 *
	 * @param uor Unidad organica del registro.
	 * @param tipoES Tipo del registro, es decir, si es de entrada o salida.
	 * @param ejercicio Año de ejercicio del registro.
	 * @param numeroRegistro Numero del registro.
	 * @param departamento Departamento del registro.
	 * @param params Parametros de conexion a la DB del usuario.
	 * @return
	 * @throws GestionSirException BDException
	 */
	public Asiento buscarAsiento(String uor, String tipoES, int ejercicio, int numeroRegistro, int departamento,
			String[] params) throws GestionSirException {

		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		Asiento asiento = null;

		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);
			GestionSirFlexiaDAO dao = GestionSirFlexiaDAO.getInstance();

			asiento = dao.buscarAsiento(uor, tipoES, ejercicio, numeroRegistro, departamento, conexion);
			asiento.setInteresados(dao.buscarInteresados(uor, tipoES, ejercicio, numeroRegistro, departamento, conexion));
			asiento.setAnexos(dao.buscarAnexos(uor, tipoES, ejercicio, numeroRegistro, departamento, conexion));

			adaptador.finTransaccion(conexion);
		} catch (GestionSirException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new GestionSirException(CodigoMensajeSir.ERROR_BUSQUEDA_INTERCAMBIO.getCodigo(),
					"Ha habido un error al recuperar el asiento", ex);
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
		return asiento;
	}

	/**
	 * Recupera los asientos que estén marcados para enviar en la base de datos. Estos asientos y sus datos relacionados
	 * son los registros guardados en las tablas R_RES, R_RED, T_TER, T_DNN, T_TVI y T_VIA.
	 *
	 * @param params
	 * @return
	 * @throws GestionSirException
	 */
	public List<Asiento> buscarAsientosParaEnviar(String[] params) throws GestionSirException {

		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		List<Asiento> asientos = null;

		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);

			asientos = GestionSirFlexiaDAO.getInstance().buscarAsientosParaEnviar(conexion);

			adaptador.finTransaccion(conexion);

		} catch (GestionSirException ex) {
			LOGGER.error("No se han podido recuperar los asientos para enviar", ex);
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("No se han podido recuperar los asientos para enviar", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_ASIENTOS_PARA_ENVIAR.getCodigo(),
					"No se han podido recuperar los asientos para enviar", ex);
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}

		return asientos;
	}

	/**
	 * Recupera los {@link Intercambio intercambios} cuyo estado pertenezca a uno del filtroEstados.
	 *
	 * @param filtroEstados Lista de {@link EstadoAsiento} de los {@link Intercambio intercambios} que seran recuperados.
	 * @param params Parametros de conexion a la base de datos.
	 * @return
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public List<Intercambio> buscarIntercambiosByEstadoAsiento(List<EstadoAsiento> filtroEstados, String[] params) throws GestionSirException {
		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		List<Intercambio> intercambios = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);

			intercambios = GestionSirDAO.getInstance().buscarIntercambiosByEstadoAsiento(filtroEstados, conexion);

			adaptador.finTransaccion(conexion);

		} catch (Exception ex) {
			LOGGER.error("Se ha producido un error: ", ex);
			StringBuilder estados = new StringBuilder(" [ ");
			for (EstadoAsiento estado : filtroEstados) {
				estados.append(String.format("Nombre: %s, Valor: %s", estado.toString(), estado.getValor()));
			}
			estados.append(" ] ");
			throw new GestionSirException(CodigoMensajeSir.ERROR_BUSCAR_ASIENTO_ESTADO.getCodigo(), String.format(
					"No se han podido recuperar los asientos pertenecientes a alguno de los siguientes estados: %s",
					estados), ex);
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
		return intercambios;
	}

	/**
	 * Recupera los datos del {@link Intercambio} y relacionados, es decir, {@link Asiento}, {@link Anexo anexos},
	 * {@linkInteresado interesados} y sus respectivos {@link Domicilio domicilios}, a partir de un codigo unico de la
	 * operacion.
	 *
	 * @param params parametros de conexion a la base de datos
	 * @param codIntercambio Codigo del asiento a recuperar
	 * @return El {@link Intercambio} completo.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public Intercambio buscarIntercambio(String codIntercambio, String[] params) throws GestionSirException {
		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);

			Intercambio intercambio = GestionSirDAO.getInstance().buscarIntercambio(codIntercambio, conexion);

			adaptador.finTransaccion(conexion);
			return intercambio;
		} catch (GestionSirException ex) {
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("Se ha producido un error al recuperar el intercambio: ", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_BUSQUEDA_INTERCAMBIO.getCodigo(),
					"No se ha podido recuperar el asiento", ex);
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
				throw new GestionSirException(CodigoMensajeSir.ERROR_BUSQUEDA_INTERCAMBIO.getCodigo(),
						"No se ha podido recuperar el asiento", ex);
			}
		}
	}

	/**
	 * Guarda los datos del {@link Asiento} y relacionados, es decir, {@link Anexo anexos},
	 * {@linkInteresado interesados} y sus respectivos {@link Domicilio domicilios} en las tablas propias de Flexia y
	 * borra las entradas relacionadas con el {@link Asiento} de las tablas temporales usadas para la gestión del SIR.
	 *
	 * @param intercambio {@link Intercambio} a confirmar en el CIR.
	 * @param uorCod Codigo de la unidad organica del usuario activo.
	 * @param params Parametros de conexion a la base de datos.
	 * @param idUsuario Identificador del usuario que da de alta los nuevos recursos.
	 * @param codDepartamento Codigo del departamento del usuario que da de alta los nuevos recursos.
	 * @param appCod Codigo de la aplicacion desde la que se dan de alta los nuevos recursos.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public void guardarAceptado(Intercambio intercambio, int uorCod, int idUsuario, int codDepartamento, int appCod, 
			String[] params) throws GestionSirException, BDException {
		Asiento asiento = intercambio.getAsiento();
		RegistroValueObject registro = GestionSirMapper.getInstance().convertir(asiento, idUsuario, appCod);

		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);
			// Se guardan terceros, domicilios, anexos y registros en las tablas de Flexia.
			registro = guardarTercerosAceptados(registro, asiento.getInteresados(), uorCod, idUsuario, appCod, 
					params, conexion);
			registro.setDptoUsuarioQRegistra(Integer.toString(codDepartamento)); // TODO ver si todos deben tener el mismo codigo de departamento o no
			registro.setIdDepOrigen(codDepartamento);
			registro.setIdentDepart(codDepartamento);
			registro.setUnidOrgUsuarioQRegistra(Integer.toString(uorCod));
			AnotacionRegistroManager.getInstance().insertRegistroValueObject(registro, params, conexion, true);
			// Actualizar Intercambio
			intercambio.setEstado(EstadoAsiento.ACEPTADO);
			GestionSirDAO.getInstance().actualizarIntercambio(prepareQueryActualizarIntercambio(intercambio), conexion);
			borrarDatosAsiento(intercambio.getAsiento().getCodAsiento(), conexion);
			intercambio.getAsiento().setNumRegistro((int) registro.getNumReg());
			adaptador.finTransaccion(conexion);
		} catch (AnotacionRegistroException ex) {
			// TODO Gestionar rollback en excepciones
			adaptador.rollBack(conexion);
			LOGGER.error("No se ha podido crear el registro");
			throw new GestionSirException(CodigoMensajeSir.ERROR_CREACION_REGISTRO.getCodigo(),
					"No se ha podido crear el registro", ex);
		} catch (SQLException ex) {
			adaptador.rollBack(conexion);
			LOGGER.error(String.format("Error al registrar el asiento aceptado: %s", ex));
			throw new GestionSirException(CodigoMensajeSir.ERROR_REGISTRO_ACEPTAR_ASIENTO.getCodigo(), 
					"Error al registrar el Asiento Aceptado");
		} catch (Exception ex) {
			try {
				adaptador.rollBack(conexion);
				LOGGER.error("Ha habido un error al aceptar el asiento", ex);
				throw new GestionSirException(CodigoMensajeSir.ERROR_ACEPTAR_ASIENTO.getCodigo(),
						"Ha habido un error", ex);
			} catch (BDException excepcionBD) {
				LOGGER.error("No se ha podido hacer rollback con exito", excepcionBD);
				throw new GestionSirException(CodigoMensajeSir.ERROR_ACEPTAR_ASIENTO.getCodigo(),
						"Ha habido un error", excepcionBD);
			}
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
	}

	/**
	 * Este metodo es el encargado de abrir la transaccion para modificar el asiento, y poder rechazarlo. Tambien se 
	 * encarga de la gestion de pasarle la query ya creada al DAO para que la ejecute.
	 *
	 * @param intercambio {@link Intercambio} cuyos datos seran eliminados.
	 * @param motivoRechazo Motivo por el que se rechaza el {@link Intercambio}.
	 * @param params Parametros de conexion de base de datos.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public void guardarRechazado(Intercambio intercambio, String motivoRechazo, String[] params) throws GestionSirException, BDException {
		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);
			intercambio.setMotivoRechazo(motivoRechazo);
			intercambio.setEstado(EstadoAsiento.RECHAZADO);
			GestionSirDAO.getInstance().actualizarIntercambio(prepareQueryActualizarIntercambio(intercambio), conexion);
			borrarDatosAsiento(intercambio.getAsiento().getCodAsiento(), conexion);
			adaptador.finTransaccion(conexion);
		} catch (BDException ex) {
			adaptador.rollBack(conexion);
			LOGGER.error("Se ha producido un error: ", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_RECHAZAR_ASIENTO.getCodigo(), "Error al rechazar el Asiento");
		} catch (SQLException ex) {
			adaptador.rollBack(conexion);
			LOGGER.error(String.format("Error al registrar el rechazo del asiento: %s", ex));
			throw new GestionSirException(CodigoMensajeSir.ERROR_REGISTRO_RECHAZO_ASIENTO.getCodigo(), 
					"Error al registrar el Asiento Rechazado");
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
	}
	
	/**
	 * Actualiza el {@link Intercambio} en base de datos.
	 *
	 * @param intercambio Intercambio a actualizar en base de datos.
	 * @param params Parametros de conexion con la base de datos.
	 */
	public void actualizarIntercambio(Intercambio intercambio, String[] params) throws GestionSirException, BDException {	
		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);
			GestionSirDAO.getInstance().actualizarIntercambio(prepareQueryActualizarIntercambio(intercambio), conexion);
			adaptador.finTransaccion(conexion);
		} catch (BDException ex) {
			adaptador.rollBack(conexion);
			LOGGER.error("Se ha producido un error: ", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_ACTUALIZAR_INTERCAMBIO.getCodigo(), "Error al actualizar el intercambio");
		} catch (SQLException ex) {
			adaptador.rollBack(conexion);
			LOGGER.error(String.format("Error al ejecutar la consulta de actualizacion del intercambio: %s", ex));
			throw new GestionSirException(CodigoMensajeSir.ERROR_ACTUALIZAR_INTERCAMBIO.getCodigo(),
					"Error al ejecutar la consulta de actualizacion del intercambio");
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
	}
	
	/**
	 * Borra todos los datos relacionados con un {@link Asiento}, a excepcion de la informacion del {@link Intercambio}.
	 * 
	 * @param codAsiento Codigo del {@link Asiento} cuyos datos seran eliminados.
	 * @param params Parametros de conexion a la base de datos.
	 * @throws GestionSirException 
	 */
	private void borrarDatosAsiento(int codAsiento, Connection conexion) throws GestionSirException {
		try {
			GestionSirDAO dao = GestionSirDAO.getInstance();
			dao.borrarDomiciliosByAsiento(codAsiento, conexion);
			dao.borrarInteresadosByAsiento(codAsiento, conexion);
			dao.borrarAnexosByAsiento(codAsiento, conexion);
			dao.borrarAsiento(codAsiento, conexion);
		} catch (SQLException ex) {
			LOGGER.error("Se ha producido un error: ", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_RECHAZAR_ASIENTO.getCodigo(), "Error al rechazar el Asiento");
		} 
	}
	
	public int recuperarCodJustificanteByIntercambio(String codIntercambio, Connection conexion) throws GestionSirException {
		SqlBuilder sql = new SqlBuilder().select("COD_JUSTIFICANTE_SIR").from("JUSTIFICANTE_SIR").whereEquals("CODIGO_INTERCAMBIO", codIntercambio);
		QueryResult queryResult = null;
		try {
			queryResult = GestionSirFlexiaDAO.getInstance().recuperarCodJustificanteByIntercambio(new SqlExecuter(sql), conexion);
		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
			//throw new GestionSirException(codigo, mensaje, parametros);
		}
		if (queryResult.next()) {
			if (queryResult != null) {
				return queryResult.getInteger("COD_JUSTIFICANTE_SIR");
			} else {
				//throw new GestionSirException(codigo, mensaje, parametros);
			}
		}
		return 0;
	}

	/**
	 * Recupera el contenido de un fichero de intercambio de la base de datos.
	 *
	 * @param codIntercambio Codigo del {@link Intercambio} cuyo fichero XML se va a recuperar.
	 * @param params parametros de conexion a la base de datos
	 * @return
	 * @throws GestionSirException
	 */
	public String cargarFicheroIntercambio(String codIntercambio, String[] params) throws GestionSirException {
		AdaptadorSQLBD adaptador = null;
		Connection conexion = null;
		try {
			adaptador = new AdaptadorSQLBD(params);
			conexion = adaptador.getConnection();
			adaptador.inicioTransaccion(conexion);
			String fichero = GestionSirDAO.getInstance().cargarFicheroIntercambio(codIntercambio, conexion);
			adaptador.finTransaccion(conexion);
			return fichero;
		} catch (BDException ex) {
			// TODO Gestionar rollback en excepciones
			LOGGER.error("Se ha producido un error: ", ex);
			throw new GestionSirException(CodigoMensajeSir.ERROR_CARGAR_FICHERO_INTERCAMBIO.getCodigo(), "Error al actualizar el intercambio");
		} catch (SQLException ex) {
			LOGGER.error(String.format("Error al ejecutar la consulta de actualizacion del intercambio: %s", ex));
			throw new GestionSirException(CodigoMensajeSir.ERROR_CARGAR_FICHERO_INTERCAMBIO.getCodigo(),
					"Error al ejecutar la consulta de actualizacion del intercambio");
		} finally {
			try {
				adaptador.devolverConexion(conexion);
			} catch (BDException ex) {
				LOGGER.error("Se ha producido un error: ", ex);
			}
		}
	}
	
	/**
	 * Metodo que construye la query para actualizar el intercambio.
	 * 
	 * @param codIntercambio Codigo del {@link Intercambio} a actualizar.
	 * @param motivoRechazo Motivo por el que se rechaza, en caso de que la actuaizacion sea para rechazar.
	 * @return La query construida.
	 */
	private SqlExecuter prepareQueryActualizarIntercambio(Intercambio intercambio) {
		SqlExecuter sql = (SqlExecuter) new SqlExecuter().update("INTERCAMBIO_SIR");
		((SqlExecuter) sql.set("ESTADO", SqlBuilder.PARAMETRO)).setValues(intercambio.getEstado().getValor());
		if (intercambio.getEstado() == EstadoAsiento.RECHAZADO) {
			sql.set("FECHA_RECHAZO", SqlBuilder.sysdate());
			sql.set("MOTIVO_RECHAZO", SqlBuilder.PARAMETRO);
			sql.addValues(intercambio.getMotivoRechazo());
		}
		sql.whereEqualsParametrizado("COD_INTERCAMBIO");
		return sql.addValues(intercambio.getCodIntercambio());
	}
	
	/**
	 * Guarda los {@link TercerosValueObject terceros} generados a partir de la lista de {@link Interesado interesados}
	 * en la base de datos. 
	 * 
	 * @param registro {@link RegistroValueObject} a actualizar y devolver.
	 * @param interesados Lista de {@link Interesado interesados} a convertir en {@link TercerosValueObject terceros}.
	 * @param uorCod Codigo de la unidad organica del usuario activo.
	 * @param idUsuario Identificador del usuario que dan de alta los nuevos recursos.
	 * @param appCod Codigo de la aplicacion desde la que se dan de alta los nuevos recursos.
	 * @param params Parametros de conexion a la base de datos.
	 * @param conexion Conexion a la base de datos
	 * @return El {@link RegistroValueObject} actualizado con los datos de los {@link TercerosValueObject terceros}.
	 * @throws GestionSirException 
	 */
	private RegistroValueObject guardarTercerosAceptados(RegistroValueObject registro, List<Interesado> interesados, 
			int uorCod, int idUsuario, int appCod, String[] params, Connection conexion) throws GestionSirException {
	
		Vector<TercerosValueObject> terceros = new Vector<TercerosValueObject>();
		// Estas listas son utilizadas por el metodo insertRegistroValueObject
		Vector<String> codigosTercero = new Vector<String>();
		Vector<String> versionesTercero = new Vector<String>();
		Vector<String> codigosDomicilio = new Vector<String>();
		Vector<String> roles = new Vector<String>();
		for (Interesado interesado : interesados) {
			TercerosValueObject tercero = guardarTerceroAceptado(interesado, uorCod, idUsuario, appCod, params, conexion);
			terceros.add(tercero);
			codigosTercero.add(tercero.getIdentificador());
			versionesTercero.add(tercero.getVersion());
			codigosDomicilio.add(((DomicilioSimpleValueObject) tercero.getDomicilios().get(0)).getIdDomicilio());
			roles.add(Integer.toString(tercero.getCodRol()));
		}
		registro.setlistaCodTercero(codigosTercero);
		registro.setlistaVersionTercero(versionesTercero);
		registro.setlistaCodDomicilio(codigosDomicilio);
		registro.setlistaRol(roles);
		return registro;
	}
	
	/**
	 * Guarda un {@link TercerosValueObject} y su domicilio principal en la base de datos creado a partir de un
	 * {@link Interesado} con formato SIR en una transaccion propia.
	 *
	 * @param interesado {@link Interesado} a guardar con formato SIR.
	 * @param uorCod Codigo de la unidad organica del usuario activo.
	 * @param idUsuario Identificador del usuario que dan de alta los nuevos recursos.
	 * @param appCod Codigo de la aplicacion desde la que se dan de alta los nuevos recursos.
	 * @param conexion Conexion con la base de datos.
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 * @return El {@link TercerosValueObject} creado con su identificador generado.
	 */
	private TercerosValueObject guardarTerceroAceptado(Interesado interesado, int uorCod, int idUsuario, int appCod,
			String[] params, Connection conexion) throws GestionSirException {
		TercerosValueObject tercero = GestionSirMapper.getInstance().convertir(interesado, idUsuario, appCod, true);

		int idTercero;
		try {
			// Un tercero siempre tiene un domicilio segun el formato del SIR
			DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) tercero.getDomicilios().get(0);
			GeneralValueObject codigoPostal = new GeneralValueObject();
			codigoPostal.setAtributo("codPais", domicilio.getIdPais());
			codigoPostal.setAtributo("codProvincia", domicilio.getIdProvincia());
			codigoPostal.setAtributo("codMunicipio", domicilio.getIdMunicipio());
			codigoPostal.setAtributo("descPostal", domicilio.getCodigoPostal());
			codigoPostal.setAtributo("defecto", "0");

			GeneralValueObject via = new GeneralValueObject();
			via.setAtributo("codPais", domicilio.getIdPais());
			via.setAtributo("codProvincia", domicilio.getIdProvincia());
			via.setAtributo("codMunicipio", domicilio.getIdMunicipio());
			via.setAtributo("descVia", domicilio.getDescVia());
			via.setAtributo("nombreCorto", ""); // TODO Que es esto
			via.setAtributo("codTipoVia", domicilio.getCodigoVia());
			via.setAtributo("usuario", idUsuario);

			tercero.setNormalizado("1"); // Poruqe se asume que es una direccion postal
			// true a dar de alta la via si no existe
			// estructuraCampos y valoresCampos establecidos a null porque no tienen campos suplementarios
			// txtNormalizado a 1 porque se asume que es una direccion postal
			// idDomicilio a null porque se usa en caso de que txtNormalizado = 2
			// transaccionExterna es true porque la transaccion debe ser la misma para todos los terceros
			idTercero = TercerosManager.getInstance().setTerceroExterno(uorCod, tercero, "1", via, false, codigoPostal, 
					null, appCod, null, null, params, conexion, true).getIdTercero();

			tercero.setIdentificador(Integer.toString(idTercero));
			return tercero;

		} catch (TechnicalException ex) {
			LOGGER.error("No se ha podido crear el tercero");
			throw new GestionSirException(CodigoMensajeSir.ERROR_CREACION_TERCERO.getCodigo(),
					"No se ha podido crear el tercero", ex);
		} catch (ErrorTransaccionalTerceroExternoException ex) {
			LOGGER.error("No se ha podido crear el tercero");
			throw new GestionSirException(CodigoMensajeSir.ERROR_CREACION_TERCERO.getCodigo(),
					"No se ha podido crear el tercero", ex);
		}

	}

	public void guardarJustificante(Justificante justificante, Connection con) throws GestionSirException {
		
			Map<String, Object> mapaParametros = new HashMap<String, Object>();
				mapaParametros.put("CERTIFICADO"				,justificante.getDsCertificado());
				mapaParametros.put("CODIGO_INTERCAMBIO"			,justificante.getCodIntercambio());
				mapaParametros.put("CODIGO_TIPO_DOCUMENTO"		,justificante.getCdTpDoc());
				mapaParametros.put("CONTENIDO_JUSTIFICANTE"		,justificante.getContenido());
				mapaParametros.put("CSV_JUSTIFICANTE"			,justificante.getCsv());
				mapaParametros.put("FECHA_HORA_PRESENTACION"	,new Timestamp(justificante.getFechaHoraPresentacion().toGregorianCalendar().getTimeInMillis()));
				mapaParametros.put("FECHA_HORA_REGISTRO"		,new Timestamp(justificante.getFechaHoraRegistro().toGregorianCalendar().getTimeInMillis()));
				mapaParametros.put("FIRMA"						,justificante.getDsFirma());
				mapaParametros.put("FIRMADO"					,justificante.isIsFirmado());
				mapaParametros.put("NOMBRE_DOC_JUSTIFICANTE"	,justificante.getNombre());
				mapaParametros.put("NUMERO_REGISTRO"			,justificante.getNumeroRegistro());
				mapaParametros.put("HASH"						,justificante.getHash());
				mapaParametros.put("ID_FICHERO_JUSTIFICANTE"	,justificante.getIdFichero());
				mapaParametros.put("TIMESTAMP_ANEXO"			,justificante.getTsAnexo());
				mapaParametros.put("TIPO_MIME"					,justificante.getDsTpMime());
				mapaParametros.put("VALIDACION_CERTIFICADO"		,justificante.getDsValCertificado());
				mapaParametros.put("VALIDEZ_DOCUMENTO"			,justificante.getCdValidez());
			try{
				GestionSirDAO.getInstance().insertarJustificante(con, new SqlExecuter().insertNextValWithValues("JUSTIFICANTE_SIR",
						"COD_JUSTIFICANTE_SIR", "JUSTIFICANTE_SIR_SEQ", mapaParametros));
			}catch(SQLException ex){
				LOGGER.error("No se ha podido crear el tercero");
				//throw new GestionSirException(codigo, mensaje, parametros);
			}
	}
	
	/**
	 * Actualiza un {@link Asiento} en la DB, incluyendo {@link Interesado interesados} y {@link Anexo anexos}
	 * relacionados.
	 *
	 * @param idRegistro
	 * @param nuevoEstadoRegistro
	 * @param conexion
	 * @throws es.altia.flexia.sir.exception.GestionSirException
	 */
	public void actualizarEstadoRegistro(Asiento asiento, int nuevoEstadoRegistro, int codJustificanteSir, Connection conexion) throws GestionSirException{
		SqlBuilder sql = new SqlBuilder()
				.updateParametrizado("R_RES", "RES_MOD", "COD_JUSTIFICANTE_SIR")
				.whereEqualsParametrizado("RES_NUM")
					.andEqualsParametrizado("RES_DEP")
					.andEqualsParametrizado("RES_EJE")
					.andEqualsParametrizado("RES_TIP")
					.andEqualsParametrizado("RES_UOD");

		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.addValues(nuevoEstadoRegistro, codJustificanteSir, 
				asiento.getNumRegistro(),
				asiento.getDepartamento(),
				asiento.getEjercicio(),
				asiento.getTipoRegistro(),
				asiento.getUnidadRegistro())
				.logSqlDebug(LOGGER);

		try {
			GestionSirFlexiaDAO.getInstance().actualizarEstadoRegistro(consulta, conexion);
		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
			//throw new GestionSirException(codigo, mensaje, parametros);
		}
	}

	public Anexo recuperarAnexoByIdFichero(String idFicheroAnexo, Connection conexion){
		
		SqlBuilder sql = new SqlBuilder().select().from("ANEXO_SIR").whereEquals("IDENTIFICADOR_FICHERO", idFicheroAnexo);
		QueryResult queryResult = null;
		try {
			queryResult = GestionSirFlexiaDAO.getInstance().recuperarCodJustificanteByIntercambio(new SqlExecuter(sql), conexion);
		} catch (SQLException ex) {
			LOGGER.error("Error al actualizar el domicilio: " + ex);
			//throw new GestionSirException(codigo, mensaje, parametros);
		}
		if (queryResult.next()) {
			try {
				return new Anexo(queryResult.getRow());
			} catch (SQLException ex) {
				java.util.logging.Logger.getLogger(GestionSirManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return null;
	}

}
