package es.altia.flexia.sir;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.EstadoAsiento;
import es.altia.flexia.sir.model.Intercambio;
import es.altia.flexia.sir.model.Justificante;
import es.altia.flexia.sir.persistence.GestionSirManager;
import es.altia.flexia.sir.soap.GestionSirClienteSOAP;
import es.altia.flexia.sir.util.GestionSirConstantes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class GestionSir {

	private final Logger LOGGER = Logger.getLogger(GestionSir.class);

	/**
	 * Recupera los datos necesarios para enviar el {@link Asiento asiento} a la oficina destino a traves de la
	 * plataforma SIR y los envia.
	 *
	 * @param uor Unidad organica del registro a enviar.
	 * @param tipoES Tipo de registro. Puede ser E o S.
	 * @param ejercicio Año de ejercicio del registro.
	 * @param numeroRegistro Numero del registro.
	 * @param departamento Codigo del departamento del registro.
	 * @param params Parametros de conexion con la base de datos.
	 * @throws GestionSirException
	 */
	public void enviarAsiento(String uor, String tipoES, int ejercicio, int numeroRegistro, int departamento,
			String[] params) throws GestionSirException, BDException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format(
					"enviarRegistro con uor: %s, tipoES: %s, ejercicio: %s, numeroRegistro: %s, departamento: %s",
					uor, tipoES, ejercicio, numeroRegistro, departamento));
		}
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(params);
		Connection conexion = adapBD.getConnection();
		try {
			Asiento asiento = GestionSirManager.getInstance().buscarAsiento(
					uor, tipoES, ejercicio, numeroRegistro, departamento, params);
			gestionarRegistro(asiento, conexion);
			adapBD.rollBack(conexion);
		} catch (BDException ex) {
			throw new GestionSirException(CodigoMensajeSir.ERROR_BUSCAR_REGISTRO.getCodigo(),
					"Ha habido un error al reguperrar el registro", ex);
		} catch (GestionSirException ex) {
			adapBD.rollBack(conexion);
			throw (ex);
		} catch (Exception ex) {
			adapBD.rollBack(conexion);
			throw new GestionSirException(CodigoMensajeSir.ERROR_ENVIAR_ASIENTO.getCodigo(),
					"Ha habido un error al reguperrar el registro", ex);
		} finally {
			adapBD.devolverConexion(conexion);
		}
	}

	/**
	 * Envia un mensaje de confirmación a través del CIR y guarda los datos en las tablas propias de Flexia, eliminando
	 * los datos del {@link Asiento} pertinente de las tablas temporales usadas para el soporte del SIR, excepto una
	 * traza en la tabla de {@link Intercambio}.
	 *
	 * @param codIntercambio Codigo de {@link Intercambio} del {@link Asiento} a aceptar.
	 * @param usuario Usuario que realiza la confirmacion del {@link Asiento}.
	 * @throws GestionSirException
	 */
	public void aceptarAsiento(String codIntercambio, UsuarioValueObject usuario) throws GestionSirException {
		String[] params = usuario.getParamsCon();
		Intercambio intercambio = GestionSirManager.getInstance().buscarIntercambio(codIntercambio, params);
		int unidadOrgCod = usuario.getUnidadOrgCod();
		GestionSirClienteSOAP.getInstance().aceptarIntercambio(
				intercambio, Integer.toString(unidadOrgCod));
		try {
			GestionSirManager.getInstance().guardarAceptado(intercambio, unidadOrgCod, usuario.getIdUsuario(),
				usuario.getDepCod(), usuario.getAppCod(), params);
		} catch (BDException ex) {
			throw new GestionSirException(CodigoMensajeSir.ERROR_ACEPTAR_ASIENTO.getCodigo(),
				"No se ha podido aceptar el asiento", ex);
		}
	}

	/**
	 * Se eliminan la mayoría de datos del asiento de la base de datos, pero se guarda parte de ellos para mantener una
	 * trazabilidad.
	 *
	 * @param codIntercambio Codigo del {@link Asiento} a rechazar.
	 * @param motivoRechazo Motivo por el que se rechaza el {@link Asiento}.
	 * @param codEntidad Codigo de la entidad que procesa el rechazo.
	 * @param params Parametros de conexion con la base de datos.
	 * @throws GestionSirException
	 */
	public void rechazarAsiento(String codIntercambio, String motivoRechazo, String codEntidad, String[] params)
			throws GestionSirException {
		//Recuperamos asiento
		Intercambio intercambio = buscarIntercambio(codIntercambio, params);
		//Llamamos al webservice para informar al Sicres
		//GestionSirClienteSOAP.getInstance().rechazarRecibido(intercambio, codEntidad, motivoRechazo);
		//Almacenamos en base de datos.
		try {
			GestionSirManager.getInstance().guardarRechazado(intercambio, motivoRechazo, params);
		} catch (BDException ex) {
			throw new GestionSirException(CodigoMensajeSir.ERROR_RECHAZAR_ASIENTO.getCodigo(),
					"No se ha podido rechazar el asiento", ex);
		}
	}

	/**
	 * Recupera los datos del {@link Asiento} y relacionados, es decir, {@link Anexo anexos},
	 * {@linkInteresado interesados} y sus respectivos {@link Domicilio domicilios}, a partir de un codigo unico
	 * autogenerado por la base de datos.
	 *
	 * @param codIntercambio Codigo unico relacionado con el {@link Intercambio}.
	 * @param params Parametros de conexion con la base de datos.
	 * @return El {@link Asiento} completo.
	 * @throws GestionSirException
	 */
	public Intercambio buscarIntercambio(String codIntercambio, String[] params) throws GestionSirException {
		Intercambio intercambio = GestionSirManager.getInstance().buscarIntercambio(codIntercambio, params);
		return intercambio;
	}

	/**
	 * Recupera los asientos ordenados de llegado más reciente a más antiguo.
	 *
	 * @param params Parametros de conexion con la base de datos.
	 * @return
	 * @throws GestionSirException
	 */
	public List<Intercambio> cargarIntercambiosPendientes(String[] params) throws GestionSirException {
		List<EstadoAsiento> filtroEstados = new ArrayList<EstadoAsiento>();
		filtroEstados.add(EstadoAsiento.PENDIENTE);
		filtroEstados.add(EstadoAsiento.FALTAN_DOCUMENTOS);
		
		List<Intercambio> asientos = GestionSirManager.getInstance().buscarIntercambiosByEstadoAsiento(filtroEstados, params);
		return asientos;
	}
	
	/**
	 * Actualiza el {@link EstadoAsiento} del {@link Intercambio} relacionado con el codigo.
	 * 
	 * @param codIntercambio Codigo del {@link Intercambio} cuyo estado sera actualizado.
	 * @param estadoAsiento {@link EstadoAsiento} que tendra el {@link Intercambio}.
	 * @param params Parametros de conexion a base de datos.
	 * @throws es.altia.flexia.sir.exception.GestionSirException 
	 */
	public void actualizarEstadoAsiento(String codIntercambio, EstadoAsiento estadoAsiento, String[] params) throws GestionSirException {
		Intercambio intercambio = new Intercambio();
		intercambio.setCodIntercambio(codIntercambio);
		intercambio.setEstado(estadoAsiento);
		try {
			GestionSirManager.getInstance().actualizarIntercambio(intercambio, params);
		} catch (BDException ex){
			throw new GestionSirException(CodigoMensajeSir.ERROR_ACTUALIZAR_INTERCAMBIO.getCodigo(),
					"No se ha podido actualizar el intercambio", ex);
		}
	}
	
	/**
	 * Recupera los {@link Asiento asientos} marcados como pendientes en la base de datos y los envía a la oficina
	 * destino a través de la plataforma SIR.
	 *
	 * @param params Parametros de conexion con la base de datos.
	 * @throws GestionSirException
	 */
	public void enviarAsientosPendientes(String[] params) throws GestionSirException, BDException {
		
		List<Asiento> listaAsientos = GestionSirManager.getInstance().buscarAsientosParaEnviar(params);
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(params);
		Connection conexion = adapBD.getConnection();
		try {
			if (listaAsientos != null && listaAsientos.size() > 0) {
				actualizarRegAsientosPentientes(listaAsientos, conexion);
			}
			adapBD.rollBack(conexion);
		} catch (GestionSirException ex) {
			adapBD.rollBack(conexion);
			throw (ex);
		} catch (Exception ex) {
			adapBD.rollBack(conexion);
		} finally {
			adapBD.devolverConexion(conexion);
		}
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
		return GestionSirManager.getInstance().cargarFicheroIntercambio(codIntercambio, params);
	}
	
	private void actualizarRegAsientosPentientes(List<Asiento> listaAsientos, Connection conexion) throws GestionSirException {
		for (Asiento asiento : listaAsientos) {
			gestionarRegistro(asiento, conexion);
		}
	}

	private void gestionarRegistro(Asiento asiento, Connection conexion) throws GestionSirException {

		int codJustificanteSir = crearJustificamenteEnvioAsiento(asiento, conexion);
		GestionSirManager.getInstance().actualizarEstadoRegistro(asiento, GestionSirConstantes.REGISTRO_SIR_ENVIADO, codJustificanteSir, conexion);

	}
	
	private int crearJustificamenteEnvioAsiento(Asiento asiento, Connection conexion) throws GestionSirException{
	
		Justificante justificante = GestionSirClienteSOAP.getInstance().enviarAsientosPendientes(asiento);
		GestionSirManager.getInstance().guardarJustificante(justificante, conexion);
		
		return GestionSirManager.getInstance().recuperarCodJustificanteByIntercambio(justificante.getCodIntercambio(), conexion);
	}
	
	public Anexo recuperarFicheroAnexoVisualizar (String idFicheroAnexo, String[] params) throws GestionSirException, BDException{
		
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(params);
		Connection conexion = adapBD.getConnection();
		return GestionSirManager.getInstance().recuperarAnexoByIdFichero(idFicheroAnexo, conexion);
	}
	
}
