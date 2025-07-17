/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.eni.persistence.GestionEniManager;
import es.altia.flexia.eni.util.GestionEniConstantes;
import es.altia.flexia.eni.util.GestionEniConversor;
import es.altia.eni.conversoreni.Constantes;
import es.altia.eni.conversoreni.domain.ColeccionIndizadaTipo;
import es.altia.eni.conversoreni.domain.Documento;
import es.altia.eni.conversoreni.domain.DocumentoContenido;
import es.altia.eni.conversoreni.domain.DocumentoContenidoBinario;
import es.altia.eni.conversoreni.domain.DocumentoIndizado;
import es.altia.eni.conversoreni.domain.EstadoExpediente;
import es.altia.eni.conversoreni.domain.Expediente;
import es.altia.eni.conversoreni.domain.ExpedienteIndizado;
import es.altia.eni.conversoreni.domain.IndiceElectronico;
import es.altia.eni.conversoreni.domain.Interesado;
import es.altia.eni.conversoreni.domain.MetadatosExpediente;
import es.altia.eni.conversoreni.domain.Organo;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import static es.altia.flexia.eni.util.GestionEniMapper.recuperarUnidadesOrganicas;
import es.altia.util.StringUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kevin
 */
public class GestionEniExpediente {

	private String numExpediente;

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionEniExpediente.class);

	/**
	 * Contructor vacio de la clase.
	 *
	 */
	public GestionEniExpediente() {
	}

	/**
	 * Contructor de la clase.
	 *
	 * @param numExpediente Numero de expediente.
	 */
	public GestionEniExpediente(String numExpediente) {
		this.numExpediente = numExpediente;

	}

	//[EXPORTACION]-------------------------------------------------------------
	/**
	 * Metodo al que se llama para empezar la exportacion del ExpedienteEni
	 *
	 * @param params Parametros de conexion de base de datos
	 */
	public Expediente generarExpedienteEni(String temp, String[] params) throws GestionEniException {
		return crearExpedienteEni(temp, params);
	}

	/**
	 * Metodo para crear el Expediente Eni
	 *
	 * @param params parametros de conexion a base de datos.
	 * @return
	 */
	private Expediente crearExpedienteEni(String temp, String[] params) throws GestionEniException {
		Expediente expedienteEni = new Expediente();
		expedienteEni.setIndice(crearIndiceElectronico(temp, params));
		expedienteEni.setMetadatosEni(crearMetadatosExpediente(params));
		expedienteEni.setVisualizacionIndice(crearVisualizacionIndice());
		return expedienteEni;
	}

	/**
	 * Metodo para crear el indice electronico.
	 *
	 * @param temp nombre del directorio temporal para la creacion de los
	 * indices
	 * @param params parametros de conexion a base de datos
	 * @return
	 */
	private IndiceElectronico crearIndiceElectronico(String temp, String[] params) throws GestionEniException {
		IndiceElectronico indiceElectronico = new IndiceElectronico();
		indiceElectronico.setContenido(crearContenidoIndiceElectronico(temp, params));
		return indiceElectronico;
	}

	/**
	 * Metodo para crear el contenido del indice Electronico
	 *
	 * @param temp nombre del directorio temporal para la creacion de los
	 * indices
	 * @param params parametros de conexion a base de datos
	 * @return
	 */
	private ExpedienteIndizado crearContenidoIndiceElectronico(String temp, String[] params) throws GestionEniException {
		ExpedienteIndizado expedienteIndizado = new ExpedienteIndizado();
		//expedienteIndizado.setColecciones(colecciones);
		expedienteIndizado.setDocumentos(crearDocumentoIndizados(temp, params));
		expedienteIndizado.setFechaIncorporacion(Calendar.getInstance());
		expedienteIndizado.setTipo(ColeccionIndizadaTipo.EXPEDIENTE);
		return expedienteIndizado;
	}

	/**
	 * Metodo que gestiona los documentos del expediente. Este metodo se engarga
	 * de consultar los documentos, guardarlos en la ruta indicada y setearlos
	 * en el ExpedienteEni.
	 *
	 * @param temp nombre del directorio temporal para la creacion de los
	 * indices
	 * @param params parametros de conexion a base de datos
	 * @return devuelve los documentos indizados del Expediente Eni
	 */
	private List<DocumentoIndizado> crearDocumentoIndizados(String temp, String[] params) throws GestionEniException {
		List<Documento> listDocumentoEni = new GestionEniDocumentos().generarListaDocumentos(this.numExpediente, params);
		List<DocumentoIndizado> listDocumentoIndizados = new ArrayList<DocumentoIndizado>();
		for (Documento documentoEni : listDocumentoEni) {
			DocumentoIndizado documentoIndizado = new DocumentoIndizado();
			documentoIndizado.setFecha(Calendar.getInstance());
			documentoIndizado.setFichero(GestionEniConversor.documentoEniToFicheroXML(documentoEni, temp));//xml generado
			//documentoIndizado.setFuncionResumen("");//otro archivo no generado si no masticado. posiblmente importar
			//documentoIndizado.setHuella(""); la huella la calcula pasandole el fichero
			documentoIndizado.setIdDocumento(documentoEni.getMetadatosEni().getIdentificador());
			documentoIndizado.setOrden(Integer.SIZE);//Integer standard
			listDocumentoIndizados.add(documentoIndizado);
		}
		return listDocumentoIndizados;
	}

	/**
	 * Metodo que genera los metadatos
	 *
	 * @param params
	 * @return
	 */
	private MetadatosExpediente crearMetadatosExpediente(String[] params) throws GestionEniException {
		
		String idExpecifico = String.format("%030d", System.currentTimeMillis());

		//[ORGANISMOS/INTERESADOS]
		List<Organo> organos = recuperarUnidadesOrganicas(this.numExpediente, params);
		List<Interesado> interesados = consultarInteresadosByExpediente(this.numExpediente, params);
		
		//[METADATOS] Creamos los metadatos
		MetadatosExpediente metadatosExpediente = new MetadatosExpediente();
		metadatosExpediente.setClasificacion(crearClasificacionMetadato(organos.get(0).getCodigo(), idExpecifico));
		metadatosExpediente.setEstado(EstadoExpediente.CERRADO);
		metadatosExpediente.setFecha(Calendar.getInstance());
		metadatosExpediente.setIdentificador(crearIdentificadorMetadato(organos.get(0).getCodigo(), idExpecifico));
		metadatosExpediente.setInteresados(interesados); // E_EXT
		metadatosExpediente.setOrganos(organos);
		metadatosExpediente.setVersionNTI(Constantes.VERSION_NTI_EXPEDIENTE);
		return metadatosExpediente;
	}

	//TODO [MOCK]
	/**
	 * Metodo para recuperar la clasificacion el Expediente Eni
	 *
	 * @return
	 */
	private String crearClasificacionMetadato(String unidadTramitadora, String IdProEspecifico)
			throws GestionEniException {
		//<Ogano>_PRO_<ID_PRO_especifico>
		StringBuilder builder = new StringBuilder();
		 builder.append(GestionEniConversor.formateadorCeros(unidadTramitadora, 9))//organo
				.append("_PRO_")
				.append(GestionEniConversor.formateadorCeros(IdProEspecifico, 30));//ID_PRO_especifico
		return builder.toString();
	}

	//TODO [MOCK]
	/**
	 * Metodo para recuperar el identificador del Expediente Eni
	 *
	 * @return
	 */
	private String crearIdentificadorMetadato(String unidadTramitadora, String IdEspecifico)
			throws GestionEniException {
		//ES_<?gano>_<AAAA>_<ID_espec?co>
		StringBuilder builder = new StringBuilder();
		 builder.append("ES_")
				.append(GestionEniConversor.formateadorCeros(unidadTramitadora, 9))//organo
				.append("_")
				.append(numExpediente.substring(0, 4))
				.append("_EXP_")
				.append(GestionEniConversor.formateadorCeros(IdEspecifico, 30));//ID_especifico
		return builder.toString();
	}

	//TODO [Esperar respuesta duda]
	private DocumentoContenido crearVisualizacionIndice() {
		if (false) {
			DocumentoContenido visualizacionIndice = new DocumentoContenidoBinario();
			InputStream fichero = GestionEniExpediente.class.getClassLoader().getResourceAsStream("ejemplo/ficheroEjemplo.txt");
			byte[] bytesFichero = new byte[10];

			try {
				fichero.read(bytesFichero);
			} catch (IOException ex) {
				LOGGER.error("No se ha encontrando el documento: " + ex);
			}
			byte[] ficheroEnBytes = bytesFichero;
			((DocumentoContenidoBinario) visualizacionIndice).setContenido(ficheroEnBytes);
			visualizacionIndice.setId("idDocumentoContenido");
			visualizacionIndice.setFormato("txt");
			return visualizacionIndice;
		}
		return null;
	}

	/**
	 * Metodo para recuperar los interesados/organismos de un expediente e
	 * insertarlos en sus correspondientes listas
	 *
	 * @param organos
	 * @param interesados
	 * @param numExpediente
	 * @param params
	 */
	private List<Interesado> consultarInteresadosByExpediente(String numExpediente, String[] params) {
		List<Interesado> interesados = new ArrayList<Interesado>();
		QueryResult resultInteresados = GestionEniManager.getInstance().getInteresadosByExpediente(numExpediente, params);
		while (resultInteresados.next()) {
				Interesado interesado = new Interesado();
				interesado.setCodigo(resultInteresados.get("TER_DOC", String.class));
				interesados.add(interesado);
		}
		return interesados;
	}

	//[IMPORTACION]-------------------------------------------------------------
	/**
	 * Metodo principal para gestionar la importacion dividiendo los expedientes
	 * en un metodo para que los gestiones y los documentos en otro
	 *
	 * @param expedienteEni Expediente Eni
	 * @param ficherosJson Ficheros leidos en formato JSON
	 * @param usuario
	 * @throws Exception
	 * @throws GestionEniException
	 */
	public String importExpediente(Expediente expedienteEni, JSONObject ficherosJson, UsuarioValueObject usuario) throws GestionEniException, Exception {
		String numeroExpediente = "";
		if (expedienteEni != null) {
			//Creamos la transaccion
			AdaptadorSQLBD adapBD = new AdaptadorSQLBD(usuario.getParamsCon());
			Connection con = adapBD.getConnection();
			try {
				adapBD.inicioTransaccion(con);
				
				numeroExpediente = getNumExpediente(usuario, con);
				if (StringUtils.isNullOrEmpty(numeroExpediente)) {
					throw new GestionEniException(CodigoMensajeEni.NUM_EXPEDIENTE_NULL_O_VACIO.getCodigo(),
							"No se ha podido generar un numero de expediente");
				}
				
				//ImportarExpediente e Interesados. Los documentos de los terceros que no tengan coincidencias son devueltos
				// TODO Llevar los tercerosNoExistentes a la pantalla
				List<String> tercerosNoExistentes = GestionEniManager.getInstance().grabarExpedienteConTerceros(
						expedienteEni, numeroExpediente, usuario, con);
				
				if(tercerosNoExistentes != null && !tercerosNoExistentes.isEmpty()){
					throw new GestionEniException(CodigoMensajeEni.ERROR_AL_RECUPERAR_TERCEROS.getCodigo(), 
							"Hay terceros no existentes", mostrarListaTerceros(tercerosNoExistentes));
				}

				//ImpotarDocumentos
				List<DocumentoIndizado> documentos = null;
				if (expedienteEni.getIndice() != null && expedienteEni.getIndice().getContenido() != null) {
					documentos = expedienteEni.getIndice().getContenido().getDocumentos();
				}
				if (documentos != null) {
					importarDocumentosExpediente(numeroExpediente, documentos, ficherosJson, usuario, con);
				}
				
				adapBD.finTransaccion(con);
			} catch (GestionEniException ex) {
				adapBD.rollBack(con);
				throw (ex);
			} finally {
				adapBD.devolverConexion(con);
			}
		}
		return numeroExpediente;
	}

	/**
	 * Metodo encargado de gestionar la importacion del documento Eni
	 *
	 * @param listaDocumentosIndizados
	 * @param path
	 * @param numExpediente
	 * @param con
	 */
	private void importarDocumentosExpediente(String numExpediente, List<DocumentoIndizado> listaDocumentosIndizados, JSONObject ficherosJson, UsuarioValueObject usuario, Connection con) throws JSONException, GestionEniException {
		for (DocumentoIndizado documentoIndizado : listaDocumentosIndizados) {
			GestionEniDocumentos documentosEni = new GestionEniDocumentos();
			String documento = ficherosJson.getString(documentoIndizado.getIdDocumento() + ".xml");
			documentosEni.importarDocumentoByString(numExpediente, documento, documentoIndizado.getIdDocumento(), usuario, con);
		}

	}

	
	private String getNumExpediente(UsuarioValueObject usuario, Connection con) {
		TramitacionValueObject tramitacionVO = new TramitacionValueObject();
		Calendar fecha = Calendar.getInstance();
		SimpleDateFormat formateadorFecha = new SimpleDateFormat("YYYY");
		String ejercicio = formateadorFecha.format(fecha.getTime());
		tramitacionVO.setEjercicioRegistro(ejercicio);

		tramitacionVO.setCodProcedimiento(GestionEniConstantes.PROCEDIMIENTO_ENI);
		try {
			// Introducimos el numero en tramitacionVO
			TramitacionDAO.getInstance().getNuevoExpediente(usuario, tramitacionVO, con);
			// numero tiene el formato <ejercicio>/<proc>/<numero>
			return tramitacionVO.getNumero();
		} catch (TramitacionException ex) {
			// TODO Gestionar
			LOGGER.error("Error en la transacción: " + ex);
		} catch (TechnicalException ex) {
			LOGGER.error("Error técnico: " + ex);
		}
		return null;
	}
	
	public String mostrarListaTerceros(List<String> tercerosNoExistentes){
		StringBuilder sb = new StringBuilder();
		for (String s : tercerosNoExistentes) {
			sb.append("\n");
			sb.append(s);
		}
		return sb.toString();
	}
	
}
