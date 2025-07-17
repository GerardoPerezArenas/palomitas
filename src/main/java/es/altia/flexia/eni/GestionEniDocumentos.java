/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.eni.persistence.GestionEniManager;
import es.altia.flexia.eni.util.GestionEniConversor;
import es.altia.flexia.eni.util.GestionEniMapper;
import es.altia.flexia.eni.util.GestionEniEnumAlias;
import es.altia.eni.conversoreni.Constantes;
import es.altia.eni.conversoreni.domain.Documento;
import es.altia.eni.conversoreni.domain.DocumentoContenido;
import es.altia.eni.conversoreni.domain.DocumentoContenidoBinario;
import es.altia.eni.conversoreni.domain.EstadoElaboracion;
import es.altia.eni.conversoreni.domain.Firma;
import es.altia.eni.conversoreni.domain.FirmaCertificadoBinario;
import es.altia.eni.conversoreni.domain.FormatoFirma;
import es.altia.eni.conversoreni.domain.MetadatosDocumento;
import es.altia.eni.conversoreni.domain.Organo;
import es.altia.eni.conversoreni.domain.Origen;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.util.GestionEniEnumTablas;
import static es.altia.flexia.eni.util.GestionEniMapper.recuperarUnidadesOrganicas;
import es.altia.util.StringUtils;
import es.altia.util.commons.MimeTypes;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.RowResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin
 */
public class GestionEniDocumentos {

	private final Logger LOGGER = Logger.getLogger(GestionEniDocumentos.class);

	
	//[EXPORTACION]-------------------------------------------------------------
	/**
	 * Metodo al que se llama para empezar la exportacion del documentoEni
	 *
	 * @param numExpediente Numero de expediente
	 * @param params Parametros de conexion de base de datos
	 */
	public List<Documento> generarListaDocumentos(String numExpediente, String[] params) throws GestionEniException {
		return crearListaDocumentoEni(numExpediente, params);
	}

	/**
	 * Metodo que llama a la lista de queries y luego las ejecuta una a una y
	 * envia su resultado a los metodos que gestionan la informacion
	 *
	 * @param numExpediente Numero de expediente para ejecutar la busqueda de
	 * los decumentos
	 * @param params Parametros para realizar la conexion
	 * @return Devuelve el documento Eni generado
	 */
	private List<Documento> crearListaDocumentoEni(String numExpediente, String[] params) throws GestionEniException {
		List<Documento> listaDocumentosEni = new ArrayList<Documento>();
		for (Map.Entry<GestionEniEnumTablas, SqlBuilder> sqlMap : GestionEniManager.getInstance().prepareQueriesDatosDocumento(numExpediente).entrySet()) {
			QueryResult result = GestionEniManager.getInstance().consultarDatosDocumento(params, sqlMap.getValue());
			if (result != null) {
				while (result.next()) {
					try {
						Documento documentoEni = crearDocumentoEni(result, numExpediente, sqlMap.getKey(), params);
						if(documentoEni != null && documentoEni.getContenido() != null){
							listaDocumentosEni.add(documentoEni);
						}
					} catch (SQLException ex) {
						LOGGER.error("Error al crear el documento ENI: ", ex);
					}
				}
			}
		}
		return listaDocumentosEni;
	}

	/**
	 * Metodo principal para crear el Documento Eni
	 *
	 * @param result Resultado de la query que devuelve los datos del documento
	 * @param numExpediente Numero del expediente que queremos exportar
	 * @param gestionEniTablasEnum Numerado con el tipo de documento a exportar
	 * @param params parametros de conexion a base de datos
	 * @return
	 * @throws SQLException 
	 */
	private Documento crearDocumentoEni(QueryResult result, String numExpediente, GestionEniEnumTablas gestionEniTablasEnum, String[] params) throws SQLException, GestionEniException {
		Documento documentoEni = obternerDatosDocumentoEni(result.getRow());
		if(documentoEni != null){
			documentoEni.setMetadatosEni(crearMetadatosDocumento(numExpediente, gestionEniTablasEnum, params));
		}
		return documentoEni;
	}

	/**
	 * Metodo para guarda en el Documento Contenido los documentos y el formato
	 * correspondiente
	 *
	 * @param result Resultado de la query que recupera los documentos
	 * @return Devuelve el documento contenido correspondiente
	 * @throws SQLException
	 */
	private Documento obternerDatosDocumentoEni(RowResult result) throws SQLException, GestionEniException {
		Documento documentoEni = new Documento();
		List<Firma> listaFirmas = new ArrayList<Firma>();
		Firma firma = new FirmaCertificadoBinario();
		DocumentoContenido documentoContenido = new DocumentoContenidoBinario();
		String nombreFichero = "";
		for (Object object : result.getKeys()) {
			switch (GestionEniEnumAlias.valueOf(object.toString())) {
				//[DOCUMENTO CONTENIDO]
					case ALIAS_DOCUMENTO_ENI_BINARIO:
						((DocumentoContenidoBinario) documentoContenido).setContenido(
								result.get(GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BINARIO.getAlias(), byte[].class));
						continue;
					case ALIAS_DOCUMENTO_ENI_BLOB:
						((DocumentoContenidoBinario) documentoContenido).setContenido(Base64.decodeBase64(
								result.getBlobBytesBase64(GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BLOB.getAlias())));
						continue;
					case ALIAS_DOCUMENTO_ENI_STRING_XML:
						java.sql.Clob ficheroXML = result.get(GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_STRING_XML.getAlias(), java.sql.Clob.class);
						if (ficheroXML != null) {
							try {
								InputStream byteDocumentoXml = ficheroXML.getAsciiStream();
								((DocumentoContenidoBinario) documentoContenido).setContenido(IOUtils.toByteArray(byteDocumentoXml));
								documentoContenido.setFormato(ConstantesDatos.EXTENSION_FICHERO_XML);
								continue;
							} catch (IOException ex) {
								LOGGER.error("No se ha podido recuperar el documento XML: ", ex);
								break;
							}
						}
						break;
				//[DOCUMENTO FIRMA]
					case ALIAS_FIRMA_DOCUMENTO:
							((FirmaCertificadoBinario)firma).setContenido(result.get(GestionEniEnumAlias.ALIAS_FIRMA_DOCUMENTO.getAlias(), byte[].class));
							firma.setFormato(FormatoFirma.CADES_DETACHED_EXPLICIT);
						continue;
					case ALIAS_FIRMA_DOCUMENTO_BLOB:
							((FirmaCertificadoBinario)firma).setContenido(result.getBlobBytes(GestionEniEnumAlias.ALIAS_FIRMA_DOCUMENTO_BLOB.getAlias()));
							firma.setFormato(FormatoFirma.CADES_DETACHED_EXPLICIT);
						continue;
				//[DOCUMENTO FORMATO]
					case ALIAS_FORMATO_DOCUMENTO:
						documentoContenido.setFormato(MimeTypes.guessMimeTypeFromExtension(
								result.get(GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO.getAlias(), String.class)));
						continue;
					case ALIAS_FORMATO_DOCUMENTO_MIME:
						documentoContenido.setFormato(result.get(GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO_MIME.getAlias(), String.class));
						continue;
					case ALIAS_FORMATO_NOMBRE_DOCUMENTO:
						nombreFichero = result.get(GestionEniEnumAlias.ALIAS_FORMATO_NOMBRE_DOCUMENTO.getAlias(), String.class);
						documentoContenido.setFormato(MimeTypes.guessMimeTypeFromFileName(nombreFichero));
						continue;
			}
		}

		if (documentoContenido != null && documentoContenido.getContenidoBytes() != null) {
			documentoEni.setContenido(documentoContenido);
			if (StringUtils.isNullOrEmpty(documentoContenido.getFormato())) {
				throw new GestionEniException(CodigoMensajeEni.ERROR_RECUPERAR_FORMATO.getCodigo(), String.format(" No se ha podido recuperar el formato del archivo %s", nombreFichero));
			}
		}
		if(firma != null && firma.getContenidoBytes() != null){
			listaFirmas.add(firma);
			documentoEni.setFirmas(listaFirmas);
		}
		
		return documentoEni;
	}

	/**
	 * Metodo para crear los Metadatos del Documento Eni
	 *
	 * @param numExpediente Numero de expediente
	 * @param params Parametros de conexion
	 * @return Devuelve los metadatos del documento
	 */
	private MetadatosDocumento crearMetadatosDocumento(String numExpediente, GestionEniEnumTablas gestionEniTablasEnum, 
			String[] params) throws GestionEniException{
		List<Organo> organos = recuperarUnidadesOrganicas(numExpediente, params);
		MetadatosDocumento metadatosDocumentoEni = new MetadatosDocumento();
		metadatosDocumentoEni.setOrganos(organos);//[Falta]
		metadatosDocumentoEni.setEstadoElaboracion(EstadoElaboracion.ORIGINAL);//ORIGINAL.En Flexia no se gestionan duplicados.Si en algun momento se soporta es necesario settear documentoEni.setIdDocumentoOrigen();
		metadatosDocumentoEni.setFecha(Calendar.getInstance());
		metadatosDocumentoEni.setIdentificador(crearIndentificadorEniDocumento(numExpediente, organos.get(0).getCodigo(), "")); //[FALTA] Creando metodo
		metadatosDocumentoEni.setOrigen(Origen.ADMINISTRACION);
		metadatosDocumentoEni.setTipoDocumental(GestionEniMapper.getTipoDocumental(gestionEniTablasEnum)); // [FALTA] Creando Metodo
		metadatosDocumentoEni.setVersionNTI(Constantes.VERSION_NTI_DOCUMENTO);
		return metadatosDocumentoEni;
	}
	
	//[MOCK] 
	/**
	 * Metodo que genera el identificador del documento.
	 *
	 * @param numExpediente Numero del expediente
	 * @return revuelve el identificador en un String
	 */
	private String crearIndentificadorEniDocumento(String numExpediente, String unidadTramitadora, 
			String idGestionDocumental) throws GestionEniException {
		//"ES_A01002823_2015_PK2JM0000000000000000000141262"
		StringBuilder builder = new StringBuilder();
		 builder.append("ES_")
				.append(GestionEniConversor.formateadorCeros(unidadTramitadora, 9))//organo
				.append("_")
				.append(numExpediente.substring(0, 4))
				.append("_")
				.append(String.format("%030d", System.currentTimeMillis()));
		return builder.toString();
	}
	
	
	//[IMPORTACION]-------------------------------------------------------------
	/**
	 * Metodo al que se llama para empezar la importacion del documentoEni
	 *
	 * @param numExpediente Numero de expediente
	 * @param documento Documento ENI en formato XML
	 * @param idDocumentoEni Documento que vamos a importar
	 * @param usuario
	 * @param con
	 * @throws GestionEniException
	 */
	public void importarDocumentoByString(String numExpediente, String documento, String idDocumentoEni, 
			UsuarioValueObject usuario, Connection con) throws GestionEniException {
		Documento documentoEni = GestionEniConversor.importarDocumentoEniByString(documento);
		if (documentoEni != null) {
				GestionEniManager.getInstance().insertarDocumentoPorTipoDocumental(documentoEni, numExpediente, usuario, con);
		}
		
	}
	
}
