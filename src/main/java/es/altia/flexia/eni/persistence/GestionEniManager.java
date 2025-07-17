/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.eni.util.GestionEniMapper;
import es.altia.flexia.eni.util.GestionEniEnumTablas;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.eni.conversoreni.domain.Documento;
import es.altia.eni.conversoreni.domain.Expediente;
import es.altia.eni.conversoreni.domain.Firma;
import es.altia.flexia.eni.util.GestionEniConstantes;
import es.altia.eni.conversoreni.domain.Interesado;
import es.altia.eni.conversoreni.domain.Organo;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.model.FirmaEni;
import es.altia.flexia.eni.util.GestionEniConversor;
import es.altia.flexia.eni.util.GestionEniEnumAlias;
import es.altia.flexia.historico.expediente.vo.CronoVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;


/**
 *
 * @author Kevin
 */
public class GestionEniManager {
	
	private static GestionEniManager instance = null;
	private final Logger LOGGER = Logger.getLogger(GestionEniManager.class);
	
	/**
	 * Intancia de la clase.
	 *
	 * @return
	 */
	public static GestionEniManager getInstance() {
		// Si no hay una instancia de esta clase tenemos que crear una
		if (instance == null) {
			// Necesitamos sincronizaci?qu?ra serializar (no multithread) las invocaciones a este metodo
			synchronized (InteresadosManager.class) {
				if (instance == null) {
					instance = new GestionEniManager();
				}
			}
		}
		return instance;
	}

	//[PREPARAR QUERIES EXPORTACION]--------------------------------------------
	/**
	 * Metodo para preparar las queries que se van a ejecutar para recuperar los
	 * documentos. En este metodo se almacena una lista de las queries que
	 * contienen los documentos necesarios para el Eni, asi podemos ejecutarlas
	 * y recuperar toda la informacion sobre los documentos que contienen.
	 *
	 * @param numExpediente Numero de expediente
	 * @return Devuelve una lista de Queries en formato SqlBuilder
	 */
	public Map<GestionEniEnumTablas, SqlBuilder> prepareQueriesDatosDocumento(String numExpediente) {
		Map<GestionEniEnumTablas, SqlBuilder> sqlFiles = new HashMap<GestionEniEnumTablas, SqlBuilder>();
		String numExpedienteString = String.format("'%s'", numExpediente);
		
		sqlFiles.put(GestionEniEnumTablas.ADJUNTO_EXT_NOTIFICACION, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("CONTENIDO",	GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BLOB.getAlias()),
						SqlBuilder.columnaConAlias("FIRMA",		GestionEniEnumAlias.ALIAS_FIRMA_DOCUMENTO.getAlias()),
						SqlBuilder.columnaConAlias("TIPO_MIME",	GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO_MIME.getAlias()))
				.from("ADJUNTO_EXT_NOTIFICACION")
				.whereEquals("NUM_EXPEDIENTE", numExpedienteString));
		
		sqlFiles.put(GestionEniEnumTablas.E_CRD, new SqlBuilder()	//TODO formato y binario
				.select(SqlBuilder.columnaConAlias("CRD_FIL", GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BINARIO.getAlias()), 
						SqlBuilder.columnaConAlias("CRD_DES", GestionEniEnumAlias.ALIAS_FORMATO_NOMBRE_DOCUMENTO.getAlias()))
				.from("E_CRD")
				.whereEquals("CRD_NUM", numExpedienteString));																	//TODO binario
		
		sqlFiles.put(GestionEniEnumTablas.E_DOC_EXT, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("DOC_EXT_FIL", GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BINARIO.getAlias()), 
						SqlBuilder.columnaConAlias("DOC_EXT_EXT", GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO.getAlias()))
				.from("E_DOC_EXT")
				.whereEquals("DOC_EXT_NUM", numExpedienteString));
		
		sqlFiles.put(GestionEniEnumTablas.E_DOCS_PRESENTADOS, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("PRESENTADO_CONTENIDO", GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BINARIO.getAlias()),
						SqlBuilder.columnaConAlias("PRESENTADO_EXTENSION", GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO.getAlias()))
				.from("E_DOCS_PRESENTADOS")
				.whereEquals("PRESENTADO_NUM", numExpedienteString));
		
		sqlFiles.put(GestionEniEnumTablas.E_TFI, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("TFI_VALOR", GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BLOB.getAlias()), 
						SqlBuilder.columnaConAlias("TFI_MIME",  GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO_MIME.getAlias()))
				.from("E_TFI")
				.whereEquals("TFI_NUM", numExpedienteString));
		
		sqlFiles.put(GestionEniEnumTablas.E_TFIT, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("TFIT_VALOR", GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_BLOB.getAlias()), 
						SqlBuilder.columnaConAlias("TFIT_MIME",  GestionEniEnumAlias.ALIAS_FORMATO_DOCUMENTO_MIME.getAlias()))
				.from("E_TFIT")
				.whereEquals("TFIT_NUM", numExpedienteString));
		
		sqlFiles.put(GestionEniEnumTablas.NOTIFICACION, new SqlBuilder()
				.select(SqlBuilder.columnaConAlias("XML_NOTIFICACION",	GestionEniEnumAlias.ALIAS_DOCUMENTO_ENI_STRING_XML.getAlias()), 
						SqlBuilder.columnaConAlias("FIRMA",				GestionEniEnumAlias.ALIAS_FIRMA_DOCUMENTO_BLOB.getAlias()))
				.from("NOTIFICACION")
				.whereEquals("NUM_EXPEDIENTE", numExpedienteString));
		return sqlFiles;
	}
	
	//[PREPARAR QUERIES IMPORTACION]--------------------------------------------
	/**
	 * Metodo para genera la query de insercion en [ADJUNTO_EXT_NOTIFICACION]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return 
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public void queryInsertAdjuntoExtNotificacion(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		String nombreFichero = GestionEniConversor.formateadorNombrefichero(documentoEni.getMetadatosEni().getIdentificador(),
				MimeTypes.getExtension(documentoEni.getContenido().getFormato()));
		
		Firma firma = null;
		if(documentoEni.getFirmas() != null ){
				firma = documentoEni.getFirmas().get(0);
		}
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("COD_MUNICIPIO", usuario.getOrgCod());														//NOT NULL
			mapaParametros.put("COD_TRAMITE", GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO);								//NOT NULL
			mapaParametros.put("FECHA", DateOperations.toTimestamp(documentoEni.getMetadatosEni().getFecha()));				//NOT NULL
			mapaParametros.put("NOMBRE", nombreFichero);																	//NOT NULL
			mapaParametros.put("NUM_EXPEDIENTE",numExpediente);																//NOT NULL
			mapaParametros.put("OCU_TRAMITE", GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA);							//NOT NULL
			mapaParametros.put("TIPO_MIME", MimeTypes.getMimeType(documentoEni.getContenido().getFormato()));				//NOT NULL
			mapaParametros.put("CONTENIDO", documentoEni.getContenido().getContenidoBytes());
			if(firma != null){
				mapaParametros.put("ESTADO_FIRMA", ConstantesDatos.ESTADO_FIRMA_FIRMADO);									//NOT NULL
				mapaParametros.put("FIRMA", firma.getContenidoBytes());
				mapaParametros.put("TIPO_CERTIFICADO_FIRMA", firma.getTipo());
				// Se insertan las firmas y se recupera el ID_INSERCION de la tabla INTERMEDIA_FIRMAS_ENI
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}else{
				mapaParametros.put("ESTADO_FIRMA", ConstantesDatos.ESTADO_FIRMA_PENDIENTE);									//NOT NULL
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("ID_NOTIFICACION", "");
//			mapaParametros.put("PLATAFORMA_FIRMA", "");
//			mapaParametros.put("COD_USUARIO_FIRMA", "");
//			mapaParametros.put("FECHA_FIRMA", "");
//			mapaParametros.put("COD_USUARIO_RECHAZO", "");
//			mapaParametros.put("FECHA_RECHAZO", "");
//			mapaParametros.put("OBSERVACIONES_RECHAZO", "");
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertNextValWithValues(GestionEniEnumTablas.ADJUNTO_EXT_NOTIFICACION.toString(), "ID", "SEQ_FILE_EXT_NOTIFICACION", mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_ADJUNTO_EXT_NOTIFICACION.getCodigo(), 
					"ADJUNTO_EXT_NOTIFICACION" ,ex);
		}
	}

	/**
	 * Metodo para genera la query de insercion en [E_CRD]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return 
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public void queryInsertDocumentoTramite(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		String nombreFichero = GestionEniConversor.formateadorNombrefichero(documentoEni.getMetadatosEni().getIdentificador(),
				MimeTypes.getExtension(documentoEni.getContenido().getFormato()));
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("CRD_DES", nombreFichero);																					//NOT NULL
			mapaParametros.put("CRD_EJE", numExpediente.substring(0,4));																	//NOT NULL
			mapaParametros.put("CRD_FAL", DateOperations.toTimestamp(documentoEni.getMetadatosEni().getFecha()));							//NOT NULL
			mapaParametros.put("CRD_MUN", usuario.getOrgCod());																				//NOT NULL
			mapaParametros.put("CRD_NUM", numExpediente);																					//NOT NULL
			mapaParametros.put("CRD_OCU", GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA);												//NOT NULL
			mapaParametros.put("CRD_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);															//NOT NULL
			mapaParametros.put("CRD_TRA", GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO);													//NOT NULL
			mapaParametros.put("CRD_USC", usuario.getIdUsuario());																			//NOT NULL
			mapaParametros.put("CRD_FIL", documentoEni.getContenido().getContenidoBytes());
			if (documentoEni.getFirmas() != null) {
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("CRD_DOC_FD", "");
//			mapaParametros.put("CRD_DOT", "");
//			mapaParametros.put("CRD_EXP_FD", "");
//			mapaParametros.put("CRD_FINF", "");
//			mapaParametros.put("CRD_FIR_EST", "");
//			mapaParametros.put("CRD_FIR_FD", "");
//			mapaParametros.put("CRD_FMO", "");
//			mapaParametros.put("CRD_ID_METADATO", "");
//			mapaParametros.put("CRD_USM", "");
			
			//Creamos la subconsulta para obtener el campo CRD_NUD [NOT NULL]
			SqlBuilder sqlBuilder = new SqlBuilder()
				.select(SqlBuilder.coalesce(String.format("%s%s%s",SqlBuilder.max("CRD_NUD"), SqlBuilder.SUMA, "1"), "1"))
				.from(GestionEniEnumTablas.E_CRD.toString())
				.whereEquals("CRD_PRO", String.format("'%s'", GestionEniConstantes.PROCEDIMIENTO_ENI))
				  .andEquals("CRD_EJE", numExpediente.substring(0, 4))
				  .andEquals("CRD_NUM", String.format("'%s'", numExpediente))
				  .andEquals("CRD_TRA", String.valueOf(GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO))
				  .andEquals("CRD_OCU", String.valueOf(GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA));
			mapaParametros.put("CRD_NUD", sqlBuilder);																						//NOT NULL [FALTA]
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues(GestionEniEnumTablas.E_CRD.toString(),mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_CRD.getCodigo(), "E_CRD" ,ex);
		}
	}

	/**
	 * Metodo para genera la query de insercion en [E_DOC_EXT]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return 
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public void queryInsertDocumentoExterno(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		Date fecha = DateOperations.toSQLTimestamp(documentoEni.getMetadatosEni().getFecha()); 
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("DOC_EXT_EJE", numExpediente.substring(0,4));										//NOT NULL
			mapaParametros.put("DOC_EXT_EXT", MimeTypes.getExtension(documentoEni.getContenido().getFormato()));	//NOT NULL
			mapaParametros.put("DOC_EXT_MUN", usuario.getOrgCod());													//NOT NULL
			mapaParametros.put("DOC_EXT_NUM", numExpediente);														//NOT NULL
			mapaParametros.put("DOC_EXT_FAL", fecha);
			mapaParametros.put("DOC_EXT_FIL", documentoEni.getContenido().getContenidoBytes());
			mapaParametros.put("DOC_EXT_NOM", documentoEni.getMetadatosEni().getIdentificador());
			mapaParametros.put("DOC_EXT_TIP", MimeTypes.getMimeType(documentoEni.getContenido().getFormato()));
			mapaParametros.put("DOC_EXT_ORIGEN",GestionEniConstantes.DOC_EXT_ORIGEN_EXTERNO); // TODO Hablar con Mila sobre los origenes
			if (documentoEni.getFirmas() != null) {
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("DOC_EXT_ID_METADATO", "");
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertNextValWithValues(GestionEniEnumTablas.E_DOC_EXT.toString(), "DOC_EXT_COD", "SEQ_E_DOC_EXT", mapaParametros));
		} catch (SQLException ex) {
				throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_DOC_EXT.getCodigo(), "E_DOC_EXT" ,ex);
		}
	}
	
	/**
	 * Metodo para genera la query de insercion en [E_DOCS_PRESENTADOS]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public void queryInsertDocumentosPresentados(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		int codDocumentoPresentado = calcularCodDocumentoPresentado(numExpediente, usuario , con);
		
		Date fecha = DateOperations.toSQLTimestamp(documentoEni.getMetadatosEni().getFecha());
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("PRESENTADO_COD_USU_ALTA", usuario.getIdUsuario());																	//NOT NULL
			mapaParametros.put("PRESENTADO_COD_DOC", codDocumentoPresentado);																		//NOT NULL
			mapaParametros.put("PRESENTADO_EJE", Integer.parseInt(numExpediente.substring(0, 4)));													//NOT NULL
			mapaParametros.put("PRESENTADO_EXTENSION", MimeTypes.getExtension(documentoEni.getContenido().getFormato()));							//NOT NULL
			mapaParametros.put("PRESENTADO_FECHA_ALTA", fecha);																						//NOT NULL
			mapaParametros.put("PRESENTADO_MUN", usuario.getOrgCod());																				//NOT NULL
			mapaParametros.put("PRESENTADO_NOMBRE", documentoEni.getMetadatosEni().getIdentificador());												//NOT NULL
			mapaParametros.put("PRESENTADO_NUM", numExpediente);																					//NOT NULL
			mapaParametros.put("PRESENTADO_ORIGEN", GestionEniConstantes.PRESENTADO_ORIGEN_BD);
			mapaParametros.put("PRESENTADO_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);															//NOT NULL
			mapaParametros.put("PRESENTADO_TIPO", MimeTypes.getMimeType(documentoEni.getContenido().getFormato()));									//NOT NULL
			mapaParametros.put("PRESENTADO_CONTENIDO", documentoEni.getContenido().getContenidoBytes());
			if (documentoEni.getFirmas() != null) {
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("PRESENTADO_COD_USU_MOD", "");
//			mapaParametros.put("PRESENTADO_FECHA_MOD", "");
			
			//Creamos la subconsulta para obtener el campo PRESENTADO_COD_DOC [NOT NULL]
			SqlBuilder sqlBuilder = new SqlBuilder().select(SqlBuilder.coalesce(String.format("%s%s%s", SqlBuilder.max("PRESENTADO_COD_DOC"), SqlBuilder.SUMA, "1"), "1"))
				.from(GestionEniEnumTablas.E_DOCS_PRESENTADOS.toString())
				.whereEquals("PRESENTADO_PRO", String.format("'%s'",GestionEniConstantes.PROCEDIMIENTO_ENI))
				  .andEquals("PRESENTADO_EJE", numExpediente.substring(0, 4))
				  .andEquals("PRESENTADO_NUM", String.format("'%s'",numExpediente));
			mapaParametros.put("PRESENTADO_COD_DOC", sqlBuilder);
			
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertNextValWithValues(GestionEniEnumTablas.E_DOCS_PRESENTADOS.toString(), "PRESENTADO_COD", "SEQ_E_DOCS_PRESENTADOS", mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_DOCS_PRESENTADOS.getCodigo(), 
					"E_DOCS_PRESENTADOS" ,ex);		}
	}
	
	/**
	 * Metodo para genera la query de insercion en [E_TFI]->[E_PCA]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return
	 * @throws es.altia.flexia.eni.exception.GestionEniException
	 * @throws SQLException 
	 */
	public void queryInsertDocumentosSuplProcedimiento(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		String campoSuplementarioTramite;
		try {
			campoSuplementarioTramite = crearCampoSuplementarioProcedimiento(usuario, con);
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_PCA.getCodigo(), "E_PCA" ,ex);
		}
		
		String nombreFichero = GestionEniConversor.formateadorNombrefichero(documentoEni.getMetadatosEni().getIdentificador(),
				MimeTypes.getExtension(documentoEni.getContenido().getFormato()));
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("TFI_COD", campoSuplementarioTramite);								//NOT NULL
			mapaParametros.put("TFI_EJE", numExpediente.substring(0,4));							//NOT NULL
			mapaParametros.put("TFI_MUN", usuario.getOrgCod());										//NOT NULL
			mapaParametros.put("TFI_NOMFICH", nombreFichero);										//NOT NULL
			mapaParametros.put("TFI_NUM", numExpediente);											//NOT NULL
			mapaParametros.put("TFI_MIME", MimeTypes.getMimeType(documentoEni.getContenido().getFormato()));
			mapaParametros.put("TFI_VALOR", documentoEni.getContenido().getContenidoBytes());
			if (documentoEni.getFirmas() != null) {
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("TFI_TAMANHO", "");
//			mapaParametros.put("TFI_ID_METADATO", "");
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues(GestionEniEnumTablas.E_TFI.toString(),mapaParametros));
		} catch (SQLException ex) {
				throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_TFI.getCodigo(), "E_TFI" ,ex);
		}
	}

	/**
	 * Metodo para genera la query de insercion en [E_TFIT]->[E_TCA]
	 * 
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return
	 * @throws es.altia.flexia.eni.exception.GestionEniException
	 * @throws SQLException 
	 */
	public void queryInsertDocumentosSuplTramite(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		String campoSuplementarioTramite;
		try {
			campoSuplementarioTramite = crearCampoSuplementarioTramite(usuario, con);
		} catch (SQLException ex) {
				throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_TCA.getCodigo(), "E_TCA" ,ex);
		}
		
		String nombreFichero = GestionEniConversor.formateadorNombrefichero(documentoEni.getMetadatosEni().getIdentificador(),
				MimeTypes.getExtension(documentoEni.getContenido().getFormato()));
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("TFIT_COD", campoSuplementarioTramite);							//NOT NULL
			mapaParametros.put("TFIT_EJE", numExpediente.substring(0,4));						//NOT NULL
			mapaParametros.put("TFIT_MUN", usuario.getOrgCod());								//NOT NULL
			mapaParametros.put("TFIT_NUM", numExpediente);										//NOT NULL
			mapaParametros.put("TFIT_OCU", GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA);	//NOT NULL
			mapaParametros.put("TFIT_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);				//NOT NULL
			mapaParametros.put("TFIT_TRA", GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO);		//NOT NULL
			mapaParametros.put("TFIT_MIME", MimeTypes.getMimeType(documentoEni.getContenido().getFormato()));
			mapaParametros.put("TFIT_NOMFICH", nombreFichero);
			mapaParametros.put("TFIT_ORIGEN", GestionEniConstantes.PRESENTADO_ORIGEN_BD);
			mapaParametros.put("TFIT_VALOR", documentoEni.getContenido().getContenidoBytes());
			if (documentoEni.getFirmas() != null) {
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("TFIT_TAMANHO", "");
//			mapaParametros.put("TFIT_ID_METADATO", "");
		
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues(GestionEniEnumTablas.E_TFIT.toString(),mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_E_TFIT.getCodigo(), "E_TFIT" ,ex);
		}
	}

	/**
	 * Metodo para genera la query de insercion en [NOTIFICACION]
	 * @param documentoEni
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @return 
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public void queryInsertNotificacion(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		
		List<Firma> firmaList = documentoEni.getFirmas();
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("NUM_EXPEDIENTE", numExpediente);									//NOT NULL 
			mapaParametros.put("COD_PROCEDIMIENTO", GestionEniConstantes.PROCEDIMIENTO_ENI);		//NOT NULL 
			mapaParametros.put("EJERCICIO", numExpediente.substring(0,4));							//NOT NULL 
			mapaParametros.put("COD_MUNICIPIO", usuario.getOrgCod());								//NOT NULL 
			mapaParametros.put("COD_TRAMITE", GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO);		//NOT NULL 
			mapaParametros.put("OCU_TRAMITE", GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA);	//NOT NULL 
		if (documentoEni.getContenido() != null && documentoEni.getContenido().getContenidoBytes() != null) {
			try {
				String documentoXML = new String(documentoEni.getContenido().getContenidoBytes(), "UTF-8");
				mapaParametros.put("XML_NOTIFICACION", documentoXML);
			} catch (UnsupportedEncodingException ex) {
				LOGGER.error("Error al tranformar los bytes del XML de NOTIFICACIONES: " + ex);
			}
		}
			
			if(firmaList != null){
				Firma firma = firmaList.get(0);
				mapaParametros.put("FIRMA", firma.getContenidoBytes());
				mapaParametros.put("FIRMADA", ConstantesDatos.ESTADO_FIRMA_FIRMADO);
				int idTablaIntermedia = insertarFirmasEni(documentoEni.getFirmas(), con);
				mapaParametros.put(FirmaEniDAO.ID_INS_INTERMEDIA, idTablaIntermedia);
			}
//			ATRIBUTOS QUE NO TENEMOS INFORMACION PARA SU SETEO.
//			mapaParametros.put("ACTO_NOTIFICADO", "");
//			mapaParametros.put("CADUCIDAD_NOTIFICACION", "");
//			mapaParametros.put("TEXTO_NOTIFICACION", "");
//			mapaParametros.put("XML_NOTIFICACION", "");
//			mapaParametros.put("FECHA_ENVIO", "");
//			mapaParametros.put("REGISTRO_RT", "");
//			mapaParametros.put("FECHA_ACUSE", "");
//			mapaParametros.put("RESULTADO", "");
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertNextValWithValues(GestionEniEnumTablas.NOTIFICACION.toString(), "CODIGO_NOTIFICACION", "SEQ_NOTIFICACION",  mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_INSERTAR_TABLA_NOTIFICACION.getCodigo(), "NOTIFICACION", ex);
		}
	}
	
	//[CONSULTAR]---------------------------------------------------------------
	/**
	 * Metodo que ejecuta las queries para consultar los datos del documento Eni
	 * @param params parametros de conexion
	 * @param sqlBuilder Sql
	 * @return 
	 */
	public QueryResult consultarDatosDocumento(String[] params, SqlBuilder sqlBuilder) {
		AdaptadorSQLBD adapt = null;
		Connection con = null;
		QueryResult queryResult = null;
		try {
			adapt = new AdaptadorSQLBD(params);
			con = adapt.getConnection();
			adapt.inicioTransaccion(con);
			sqlBuilder.logSqlDebug(LOGGER);
			queryResult = GestionEniDAO.getInstance().executeQuery(con, sqlBuilder);
		} catch (BDException ex) {
			LOGGER.error("Error al consultar los documentos: " + ex);
		}finally{
			try {
				adapt.devolverConexion(con);
			} catch (BDException ex) {
				LOGGER.error("Error al cerrar la conexion: " + ex);
			}
		}
		return queryResult;
	}

	/**
	 * Metodo que llama al manager de Interesados para recuperar los interesados
	 * del expediente.
	 *
	 * @param numExpediente Numero de Expediente
	 * @param params parametros de conexion
	 * @return Devuelve un QueryResult con la lista de interesados
	 */
	public QueryResult getInteresadosByExpediente(String numExpediente, String[] params) {
		return InteresadosManager.getInstance().getInteresadosByExpediente(numExpediente, params);
	}

	/**
	 * Metodo para recuperar las unidades organicas de un expediente
	 *
	 * @param params Parametros de la conexion
	 * @param numExpediente Numero de expediente
	 * @return revuelve el UOR [UNIDAD_ORGANICA]
	 */
	public QueryResult recuperarUnidadesOrganicasByExp(String[] params, String numExpediente) {
		AdaptadorSQLBD adapt = null;
		Connection con = null;
		QueryResult queryResult = null;
		try {
			adapt = new AdaptadorSQLBD(params);
			con = adapt.getConnection();
			adapt.inicioTransaccion(con);
			queryResult = GestionEniDAO.getInstance().executeQuery(con, new SqlBuilder()
					.select(SqlBuilder.columnaConAlias("uor.UOR_COD_VIS", "UNIDAD_ORGANICA"))
					.from("E_EXP exp").innerJoin("A_UOR uor", "exp.EXP_UOR", SqlBuilder.IGUAL, "uor.UOR_COD")
					.whereEquals("exp.EXP_NUM", String.format("'%s'", numExpediente)));
		} catch (BDException ex) {
			LOGGER.error("Error al recuperar las unidades Organicas" + ex);
		}finally{
			try {
				adapt.devolverConexion(con);
			} catch (BDException ex) {
				LOGGER.error("Error al cerrar la conexion: " + ex);
			}
		}
		return queryResult;
	}
	
	//[INSERTAR]----------------------------------------------------------------
	/**
	 * Metodo que inserta los datos en sus respectivas tablas de base de datos.
	 *
	 * @param documentoEni Documento que se va a insertar
	 * @param numExpediente Numero de expediente
	 * @param usuario Usuario de la session
	 * @param con conexion a base de datos
	 * @throws es.altia.flexia.eni.exception.GestionEniException
	 */
	public void insertarDocumentoPorTipoDocumental(Documento documentoEni, String numExpediente, UsuarioValueObject usuario, Connection con) throws GestionEniException {

			GestionEniMapper.getQueryByTipoDocumental(documentoEni, usuario, numExpediente, con);
			//GestionEniDAO.getInstance().insertarQuery(con, GestionEniMapper.getQueryByTipoDocumental(documentoEni, usuario, numExpediente, con));

	}

	/**
	 * Metodo encargado de gestionar las inserciones del expediente.
	 * 
	 * @param expedienteEni Expediente Eni importado. Prerrequisito: No es null
	 * @param numExpediente Numero de expediente a introducir en el expediente
	 * @param codUsuario Codigo del usuario ejecutando la aplicacion
	 * @param codUorInicio Codigo de la unidad organica ejecutando la aplicacion
	 * @param con Conexion a base de datos
	 * @return 
	 * @throws Exception 
	 */
	public List<String> grabarExpedienteConTerceros(Expediente expedienteEni, String numExpediente, UsuarioValueObject usuario, 
			Connection con) throws Exception {

		//Comprobamos que la Unidad Organica exista en Flexia.
		int uorCodigo = comprobarUnidadOrganica(expedienteEni.getMetadatosEni().getOrganos().get(0).getCodigo(),con);
		if(uorCodigo <= 0 ){
			throw new GestionEniException(numExpediente, numExpediente);
		}
		
		//Metodo encargado de mappear el Expdiente Eni a un Expediente VO
		ExpedienteDAO expDAO = ExpedienteDAO.getInstance();
		ExpedienteVO expediente = GestionEniMapper.mapearExpedienteVO(expedienteEni, numExpediente, usuario, 
				uorCodigo);
		expDAO.grabarExpediente(expediente, con);
		
		//Metodo encargado de mappear la relacion de tramite y expediente e insertarla en E_Cro
		ArrayList<CronoVO> listaeCro = GestionEniMapper.mapearListaCroVO(expediente);
		expDAO.grabarTramitesExpediente(listaeCro, con);
		
		
		//Recuperamos los Roles
		QueryResult resultado = recuperarCodRolesEni(con);
		resultado.next();
		Integer codRolInteresado = resultado.getInteger("ROL_COD");
		resultado.next();
		Integer codRolOrganizacion = resultado.getInteger("ROL_COD");
		
		//Llenamos una lista con los interesados, que serian los que vienen en la lista de
		//interesados, mas, los que vienen en la lista de organos menos el primer organo.
		List<String> tercerosNoExistentes = null;
		if (expedienteEni.getMetadatosEni() != null) {
			Map<String, Integer> mapaDocsRoles = new HashMap<String, Integer>();
			for (Interesado interesado : expedienteEni.getMetadatosEni().getInteresados()) {
				mapaDocsRoles.put(interesado.getCodigo(), codRolInteresado);
			}
			List<Organo> listaOrganos = expedienteEni.getMetadatosEni().getOrganos();
			if (listaOrganos != null && !listaOrganos.isEmpty()) {
				if (listaOrganos.size() > 1) {
					for (int i = 1; i < listaOrganos.size(); i++) {
						mapaDocsRoles.put(listaOrganos.get(i).getCodigo(), codRolOrganizacion);
					}
				}
			} else {
				throw new GestionEniException(numExpediente, numExpediente);
			}

			// Si no hay interesados u organos, no continuamos esta parte del procedimiento
			if (!mapaDocsRoles.isEmpty()) {
				tercerosNoExistentes = grabarTercerosExpediente(expediente, mapaDocsRoles, con);
			}
		}
		return tercerosNoExistentes;
}

	/**
	 * Recupera todos los terceros que esten relacionados con cualquiera de los documentos pasados por parametro y
	 * guarda aquellos que coincidan con el criterio de busqueda. Solo recupera, para cada uno de los documentos, la 
	 * version mas reciente del tercero asociado.
	 *
	 * @param mapaDocsRoles relaciona documentos con el rol adecuado al tipo de documento que sea
	 * @param con Conexion a base de datos
	 * @return
	 */
	private List<String> grabarTercerosExpediente(ExpedienteVO expediente, Map<String, Integer> mapaDocsRoles, Connection con) 
			throws SQLException, Exception {
		
		// Se recuperan los terceros relacionados con uno de los documentos
		List<String> documentos = new ArrayList<String>();
		documentos.addAll(mapaDocsRoles.keySet());
		//comprobamos los terceros que existen
		QueryResult resultado = GestionEniDAO.getInstance().getInteresadosByListaDocumentos(documentos, con);
		List<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
		while (resultado.next()) {
			TercerosValueObject tercero = new TercerosValueObject(resultado.getRow());
			// Se asigna el rol al tercero
			tercero.setCodRol(mapaDocsRoles.get(tercero.getDocumento()));
			terceros.add(tercero);
		}

		//Inserccion terceros
		ArrayList<InteresadoExpedienteVO> interesados = (ArrayList<InteresadoExpedienteVO>) GestionEniConversor
				.listaTercerosValueObjectToListaInteresadosExpedienteVO(expediente, terceros);
		ExpedienteDAO.getInstance().grabarListaInteresadoExpediente(interesados, con);
		
		
		//filtrmoa los que existen y devolvemos los que no
		List<String> tercerosNoExistentes = null;
		// Hay alguno que no encontro coincidencia en la base de datos
		if (terceros.size() != documentos.size()) {
			boolean existe;
			tercerosNoExistentes = new ArrayList<String>();
			for (String documento : documentos) {
				existe = false;
				for (TercerosValueObject tercero : terceros) {
					// Siempre tienen documento dado que la busqueda se hace mediante documentos
					if (tercero.getDocumento().equals(documento)) {
						existe = true;
						break; // No se mira el resto porque se ha encontrado coincidencia
					}
				}
				if (!existe) {
					tercerosNoExistentes.add(documento);
				}
			}
		}
		return tercerosNoExistentes;
	}

	/**
	 * Crea y ejecuta una consulta a la base de datos para recuperar los codigos de los roles usados por el 
	 * procedimiento ENI. 
	 * Debe devolver primero el rol asociado a Interesado y despues el asociado a Organizacion.
	 * 
	 * @param con
	 * @return 
	 */
	private QueryResult recuperarCodRolesEni(Connection con) {
		// TODO Cambiar el mensaje error de "Error al recuperar tercero"?
		SqlBuilder sql = new SqlBuilder()
				.select("ROL_COD")
				.from("E_ROL")
				.whereIn("ROL_DES", GestionEniConstantes.ROL_INTERESADO, GestionEniConstantes.ROL_ORGANIZACION) 
				.andEquals("ROL_PRO", String.format("'%s'", GestionEniConstantes.PROCEDIMIENTO_ENI))
				// De forma que conozcamos que campo va primero y cual despues
				.orderBy("ROL_DES");
		return GestionEniDAO.getInstance().executeQuery(con, sql);
	}
	
	//[CAMPOS SUPLEMENTARIOS E_TCA/E_PCA]---------------------------------------
	/**
	 * Metodo para recuperar los errores del calculador de Codigo y el insetado
	 * y devolver el error correspondiente al padre.
	 * @param con Conexion a base de datos
	 * @throws SQLException 
	 */
	public String crearCampoSuplementarioTramite(UsuarioValueObject usuario, Connection con) throws SQLException {
		int numero = 0;
		try {
			numero = calcularCodigoCampoSuplementarioTramite(con);
		} catch (SQLException ex) {
			throw new SQLException("Error al consultar Campo Suplementario de tramite: " + ex);
		}
		try {
			return insertarCodigoCampoSuplementarioTramite(usuario, numero, con);
		} catch (SQLException ex) {
			throw new SQLException("Error al Insertar el nuevo Campo Suplementario de tramite: " + ex);
		}
		
	}
	
	/**
	 * Metodo para recuperar los errores del calculador de Codigo y el insetado
	 * y devolver el error correspondiente al padre.
	 * 
	 * @param usuario
	 * @param con Conexion a base de datos
	 * @return 
	 * @throws SQLException 
	 */
	public String crearCampoSuplementarioProcedimiento(UsuarioValueObject usuario, Connection con) throws SQLException {
		int numero = 0;
		try {
			numero = calcularCodigoCampoSuplementarioProcedimiento(con);
		} catch (SQLException ex) {
			throw new SQLException("Error al consultar Campo Suplementario de tramite: " + ex);
		}
		try {
			return insertarCodigoCampoSuplementarioProcedimiento(usuario, numero, con);
		} catch (SQLException ex) {
			throw new SQLException("Error al Insertar el nuevo Campo Suplementario de tramite: " + ex);
		}
	}
	
	/**
	 * Metodo para calcular el codigo de campo suplementario.
	 * @param con conexion de base de datos
	 * @return
	 * @throws SQLException 
	 */
	private int calcularCodigoCampoSuplementarioTramite(Connection con) throws SQLException {
		QueryResult queryResult = null;

		SqlBuilder sqlBuilder = new SqlBuilder().select(SqlBuilder.countAllAs(GestionEniConstantes.COUNT_COD_SUPLEMENTARIO))
				.from("E_TCA")
				.whereLikeFinal("TCA_COD", GestionEniConstantes.DES_CAMPO_SUPLEMENTARIO_ENI)
				.andEquals("TCA_PRO", String.format("'%s'", GestionEniConstantes.PROCEDIMIENTO_ENI));

		//EJECUTAMOS LA QUERIE ES DE ARRIBA
		queryResult = GestionEniDAO.getInstance().executeQuery(con, sqlBuilder);

		int numero = 0;
		if ((queryResult != null) && (queryResult.next())) {
			numero = queryResult.get("COD_SUPL", BigDecimal.class).intValue();
		} else {
			LOGGER.info("No se ha obtenido nungun campo suplementario de Tramite. Se creará uno para el Expediente ");
		}
		return ++numero;
	}
	
	/**
	 * Metodo para calcular el codigo de campo suplementario.
	 * @param con conexion de base de datos
	 * @return
	 * @throws SQLException 
	 */
	private int calcularCodigoCampoSuplementarioProcedimiento(Connection con) throws SQLException {
		QueryResult queryResult = null;

		SqlBuilder sqlBuilder = new SqlBuilder().select(SqlBuilder.countAllAs(GestionEniConstantes.COUNT_COD_SUPLEMENTARIO))
				.from("E_PCA")
				.whereLikeFinal("PCA_COD", GestionEniConstantes.DES_CAMPO_SUPLEMENTARIO_ENI)
				.andEquals("PCA_PRO", String.format("'%s'", GestionEniConstantes.PROCEDIMIENTO_ENI));

		//EJECUTAMOS LA QUERIE ES DE ARRIBA
		queryResult = GestionEniDAO.getInstance().executeQuery(con, sqlBuilder);

		int numero = 0;
		if ((queryResult != null) && (queryResult.next())) {
			numero = queryResult.get("COD_SUPL", BigDecimal.class).intValue();
		} else {
			LOGGER.info("No se ha obtenido nungun campo suplementario de Procedimiento. Se creará uno para el Expediente ");
		}
		return ++numero;
	}
	
	/**
	 * Metodo para insertar en la tabla E_TCA el codigo del nuevo campo suplementario.
	 * @param numero 
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private String insertarCodigoCampoSuplementarioTramite(UsuarioValueObject usuario, int numero, Connection con) throws SQLException{
		
		String codigoCamposSuplTra = String.format("%s%s", GestionEniConstantes.DES_CAMPO_SUPLEMENTARIO_ENI, numero);
		
		//EJECUTAMOS EL INSERT SI ES NECESARIO
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("TCA_COD", codigoCamposSuplTra);																//NOT NULL
			mapaParametros.put("TCA_DES", codigoCamposSuplTra);																//NOT NULL
			mapaParametros.put("TCA_MUN", usuario.getOrgCod());																//NOT NULL
			mapaParametros.put("TCA_NOR", "0");																				//NOT NULL
			mapaParametros.put("TCA_OBL", "0");																				//NOT NULL
			mapaParametros.put("TCA_PLT", ConstantesDatos.TIPO_CAMPO_FICHERO);												//NOT NULL
			mapaParametros.put("TCA_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);											//NOT NULL
			mapaParametros.put("TCA_ROT", codigoCamposSuplTra);																//NOT NULL
			mapaParametros.put("TCA_TAM", "0");																				//NOT NULL
			mapaParametros.put("TCA_TDA", ConstantesDatos.TIPO_CAMPO_FICHERO);												//NOT NULL
			mapaParametros.put("TCA_TRA", "1");																				//NOT NULL
			mapaParametros.put("TCA_VIS", ConstantesDatos.S);																//NOT NULL
			mapaParametros.put("PCA_GROUP","DEF");								//TODO
			mapaParametros.put("TCA_ACTIVO", ConstantesDatos.SI);
			mapaParametros.put("TCA_BLOQ", ConstantesDatos.NO);
			mapaParametros.put("TCA_OCULTO", ConstantesDatos.NO);
//			mapaParametros.put("PERIODO_PLAZO","");
//			mapaParametros.put("PLAZO_AVISO","");
//			mapaParametros.put("TCA_DESPLEGABLE","");
//			mapaParametros.put("TCA_MAS","");
//			mapaParametros.put("TCA_POS_X","");
//			mapaParametros.put("TCA_POS_Y","");
		
		GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues("E_TCA",mapaParametros));
		
		return codigoCamposSuplTra;
	}
	
	
	/**
	 * Metodo para insertar en la tabla E_PCA el codigo del nuevo campo suplementario.
	 * @param numero 
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private String insertarCodigoCampoSuplementarioProcedimiento(UsuarioValueObject usuario, int numero, Connection con) throws SQLException{
		
		String codigoCamposSuplPro = String.format("%s%s", GestionEniConstantes.DES_CAMPO_SUPLEMENTARIO_ENI, numero);
		
		//EJECUTAMOS EL INSERT SI ES NECESARIO
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("PCA_COD", codigoCamposSuplPro);																//NOT NULL
			mapaParametros.put("PCA_DES", codigoCamposSuplPro);																//NOT NULL
			mapaParametros.put("PCA_MUN", usuario.getOrgCod());																//NOT NULL
			mapaParametros.put("PCA_NOR", "0");																				//NOT NULL
			mapaParametros.put("PCA_OBL", "0");																				//NOT NULL
			mapaParametros.put("PCA_PLT", ConstantesDatos.TIPO_CAMPO_FICHERO);												//NOT NULL
			mapaParametros.put("PCA_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);											//NOT NULL
			mapaParametros.put("PCA_ROT", codigoCamposSuplPro);																//NOT NULL
			mapaParametros.put("PCA_TAM", "0");																				//NOT NULL
			mapaParametros.put("PCA_TDA", ConstantesDatos.TIPO_CAMPO_FICHERO);												//NOT NULL
			mapaParametros.put("PCA_ACTIVO", ConstantesDatos.SI);
			mapaParametros.put("PCA_BLOQ", ConstantesDatos.NO);
			mapaParametros.put("PCA_OCULTO", ConstantesDatos.NO);
//			mapaParametros.put("PERIODO_PLAZO","");
//			mapaParametros.put("PCA_DESPLEGABLE","");
//			mapaParametros.put("PCA_GROUP","DEF");
//			mapaParametros.put("PCA_MAS","");
//			mapaParametros.put("PCA_POS_X","");
//			mapaParametros.put("PCA_POS_Y","");
//			mapaParametros.put("PLAZO_AVISO","");
		
		GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues("E_PCA",mapaParametros));
		
		return codigoCamposSuplPro;
	}

	/**
	 * Inserta las firmas en la tabla FIRMAS_ENI e inserta las filas apropiadas en INTERMEDIA_FIRMAS_ENI que relacionen
	 * un identificador con dichas firmas, que devuelve una vez insertadas todas las firmas.
	 * 
	 * @param firmas Firmas a insertar de un documento.
	 * @param con Conexion abierta con la base de datos.
	 * @return Identificador de insercion que se relaciona con las firmas insertadas.
	 */
	private int insertarFirmasEni(List<Firma> firmas, Connection con) throws GestionEniException {
		try {
			Integer idInsercion = null;
			for (Firma firma : firmas) {
				FirmaEni firmaEni = new FirmaEni(firma);
				FirmaEni insertada = FirmaEniDAO.getInstance().insertar(firmaEni, con);
				// idInsercion toma valor en la primera insercion, las demas firmas estan relacionadas con el mismo id
				idInsercion = FirmaEniDAO.getInstance().insertarFilaTablaIntermedia(
						idInsercion, insertada.getId(), con);
			}
			return idInsercion;
		} catch (SQLException ex) {
			throw new GestionEniException(
					CodigoMensajeEni.ERROR_INSERTAR_FIRMA.toString(), "No ha sido posible insertar la firma", ex);
		}
	}

	//[DOCUMENTOS PRESENTADOS E_DOP/E_DOE]--------------------------------------
	private int calcularCodDocumentoPresentado(String numExpediente, UsuarioValueObject usuario, Connection con) throws GestionEniException {
		int cantidadDOP = countTipoDocumentosExpediente(numExpediente, con);
		int cantidadDOE = countReferenciadeDocumentosExpediente(numExpediente, con);

		if (cantidadDOP == cantidadDOE) {
			//Insert en la tabla E_DOP y posteriormente en E_DOE
			insertarTipoDocumentosExpediente(String.format("%d", ++cantidadDOP), usuario, numExpediente, con);
			return cantidadDOP;
		} else {
			//Insert en la tabla E_DOE
			insertarReferenciadeDocumentosExpediente(String.format("%d", ++cantidadDOE), usuario, numExpediente, con);
			return cantidadDOE;
		}
	}
	
	private int countTipoDocumentosExpediente(String numExpediente, Connection con) throws GestionEniException {
		QueryResult queryResultDOP = null;

		SqlBuilder sqlBuilderDOP = new SqlBuilder()
				.select(SqlBuilder.countAllAs("DOP_SIZE"))
				.from("E_DOP")
				.whereEquals("DOP_PRO", String.format("'%s'", numExpediente.split(ConstantesDatos.BARRA)[1]));

		queryResultDOP = GestionEniDAO.getInstance().executeQuery(con, sqlBuilderDOP);
		int numero = 0;
		if ((queryResultDOP != null) && (queryResultDOP.next())) {
			numero = queryResultDOP.get("DOP_SIZE", BigDecimal.class).intValue();
		} else {
			LOGGER.info("No se ha obtenido ningun valor en E_DOP. Se creará uno para el Expediente ");
		}
		return numero;
	}
	
	private void insertarTipoDocumentosExpediente(String codigoDop, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException{
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		
			mapaParametros.put("DOP_COD", codigoDop);								//NOT NULL
			mapaParametros.put("DOP_ESTADO", 1);									//NOT NULL
			mapaParametros.put("DOP_MUN", usuario.getOrgCod());						//NOT NULL
//			mapaParametros.put("DOP_OBL", "");
//			mapaParametros.put("DOP_ORDEN", "");
			mapaParametros.put("DOP_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);	//NOT NULL
//			mapaParametros.put("DOP_TDO", "");
		
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues("E_DOP", mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_CONSULTAR_TABLA_E_DOP.getCodigo(), "E_DOP", ex);
		}
		
		/*Si tenemos que insertar en la tabla E_DOP es que no hay suficiente espacio en la tabla E_DOE
		por lo tanto tambien es necesario insertar en ella.*/
		insertarReferenciadeDocumentosExpediente(codigoDop, usuario, numExpediente, con);
	}
	
	private int countReferenciadeDocumentosExpediente(String numExpediente, Connection con) throws GestionEniException {
		QueryResult queryResultDOE = null;

		SqlBuilder sqlBuilderDOE = new SqlBuilder()
				.select(SqlBuilder.countAllAs("DOE_SIZE"))
				.from("E_DOE")
				.whereEquals("DOE_NUM", String.format("'%s'", numExpediente));
		
		queryResultDOE = GestionEniDAO.getInstance().executeQuery(con, sqlBuilderDOE);
		int numero = 0;
		if ((queryResultDOE != null) && (queryResultDOE.next())) {
			numero = queryResultDOE.get("DOE_SIZE", BigDecimal.class).intValue();
		} else {
			LOGGER.info("No se ha obtenido ningun valor en E_DOP. Se creará uno para el Expediente ");
		}
		return numero;
	}
	
	/***
	 * Metodo para insertar un nuevo campo en la tabla E_DOE en caso de que se necesite insertar mas datos
	 * de los que necesitaban al inicio del expediente
	 * @param codigoDop
	 * @param usuario
	 * @param numExpediente
	 * @param con
	 * @throws GestionEniException 
	 */
	private void insertarReferenciadeDocumentosExpediente(String codigoDop, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException{
		
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		
		mapaParametros.put("DOE_COD", codigoDop);											// NOT NULL NUMBER(3)
		mapaParametros.put("DOE_EJE", Integer.parseInt(numExpediente.substring(0, 4)));		// NOT NULL NUMBER(4)
		mapaParametros.put("DOE_FEC", DateOperations.toSQLTimestamp(Calendar.getInstance()));
		mapaParametros.put("DOE_MUN", usuario.getOrgCod());									// NOT NULL NUMBER(3)
		mapaParametros.put("DOE_NUM", numExpediente);										// NOT NULL VARCHAR2(30)
		mapaParametros.put("DOE_PRO", GestionEniConstantes.PROCEDIMIENTO_ENI);				// NOT NULL VARCHAR2(5)	
		try {
			GestionEniDAO.getInstance().insertarQuery(con, new SqlExecuter().insertWithValues("E_DOE", mapaParametros));
		} catch (SQLException ex) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_CONSULTAR_TABLA_E_DOE.getCodigo(), "E_DOE", ex);
		}
	}

	//[Unidad ORGANICA]---------------------------------------------------------
	/**
	 * Metodo para comprobar si ya existe la unidad tramitadora del expediente.
	 * @param uorCodigoVisible Codigo Visible de la unidad tramitadora.
	 * @param con
	 * @return 
	 */
	private int comprobarUnidadOrganica(String uorCodigoVisible, Connection con){
		QueryResult queryResult = null;
		
		SqlBuilder sqlBuilder = new SqlBuilder()
				.select("UOR_COD")
				.from("A_UOR")
				.whereEquals("UOR_COD_VIS", String.format("'%s'", uorCodigoVisible));
		
		queryResult = GestionEniDAO.getInstance().executeQuery(con, sqlBuilder);
		if ((queryResult != null) && (queryResult.next())) {
			return queryResult.get("UOR_COD", BigDecimal.class).intValue();
		} else {
			LOGGER.info("No se ha obtenido ningun valor en A_UOR");
		}
		return 0;
	}
	
}

