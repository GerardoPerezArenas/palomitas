/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni;

import es.altia.flexia.eni.util.DirectorioTemporal;
import es.altia.flexia.eni.util.GestionEniConstantes;
import es.altia.flexia.eni.util.GestionEniConversor;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.eni.conversoreni.ConversorEniException;
import es.altia.eni.conversoreni.domain.DocumentoIndizado;
import es.altia.eni.conversoreni.domain.Expediente;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.util.TiposRegistro;
import es.altia.flexia.eni.util.DocumentNotFoundException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kevin
 */
public class GestionEni {

	private static final Logger LOGGER = Logger.getLogger(GestionEni.class);

	/**
	 * Metodo para gestionar la exportacion del Expediente eni
	 *
	 * @param numExpediente Codigo del expediente que vamos a exportar
	 * @param request Request
	 * @throws es.altia.eni.conversoreni.ConversorEniException
	 * @throws es.altia.common.exception.TechnicalException
	 */
	public String generarDocumentoEni(String numExpediente, UsuarioValueObject usuario) 
			throws ConversorEniException, TechnicalException, GestionEniException {
		
		String[] params = usuario.getParamsCon(); // TODO cambiar HttpServletRequest request por String[] params
		
		// Se crea la carpeta temporal
		String fechaActual = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String carpetaTemporal = String.format("ENI_%s_%s", numExpediente.replaceAll("\\/", "_"), fechaActual);
		GestionEniExpediente gestionEniExpediente = new GestionEniExpediente(numExpediente);
		Expediente expedienteEni = gestionEniExpediente.generarExpedienteEni(carpetaTemporal, params);
		List<String> listaDocumentosExportar = new ArrayList<String>();
		
		if (expedienteEni != null) {
			GestionEniConversor.expedienteEniToFicheroXML(expedienteEni, carpetaTemporal);
		
			if (expedienteEni.getIndice() != null && expedienteEni.getIndice().getContenido() != null) {
				for (DocumentoIndizado documento : expedienteEni.getIndice().getContenido().getTodosDocumentos()) {
					listaDocumentosExportar.add(String.format("%s%s", documento.getIdDocumento(), ConstantesDatos.EXTENSION_FICHERO_XML_CON_PUNTO));
				}
			}
		}
		String nombreFichero = String.format("%s%s", carpetaTemporal, ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_ZIP);
		//creacion del objeto DirectorioTemporal para el control de eliminacion de archivos cuando sucede algun fallo, o termina satisfactoriamente
		DirectorioTemporal dirTemp = new DirectorioTemporal();
		String pathExportEni = ResourceBundle.getBundle("resources/paths").getString(GestionEniConstantes.PATH_EXPORT_ENI);
		
		String temporal = String.format("%s%s%s", pathExportEni, carpetaTemporal, ConstantesDatos.BARRA_INVERTIDA);
		dirTemp.setPath(temporal);
		try {
			if (listaDocumentosExportar.size() > 0) {
				listaDocumentosExportar.add(GestionEniConstantes.INDEX_XML);
				List <String> archivos = new ArrayList<String>();
				for (String docu : listaDocumentosExportar){
					archivos.add(docu);
				}
				dirTemp.setArchivos(archivos);
				guardarRegistroExp(TiposRegistro.EXPORTACION,usuario,numExpediente,usuario.getParamsCon());
				return GestionEniConversor.comprimirFilesZip(pathExportEni, dirTemp , nombreFichero);
			}
		}catch (IOException ex) {
			LOGGER.error("Error al comprimir los documentos : ", ex);
		}finally{
			eliminarDirectorioTemporal(dirTemp);
		}
		return null;
	}

	/**
	 * Metodo de conveniencia para obtener las claves de un documento Json
	 * @param ficherosJson
	 * @return 
	 */
	private List<String> obtenerClaves (JSONObject ficherosJson){
		Iterator iterador = ficherosJson.keys();
		List<String> claves = new ArrayList<String>();
		while(iterador.hasNext()){
			String clave = (String) iterador.next();
			claves.add(clave);
		}
		return claves;
	}
	
	/**
	 * Metodo para obtener el expediente Eni entre los documentos el Zip
	 *
	 * @param ficheros
	 * @param usuario
	 * @throws DocumentNotFoundException 
	 * @throws org.json.JSONException 
	 * @throws es.altia.flexia.eni.exception.GestionEniException 
	 */
	public String obtenerExpedienteEni(UsuarioValueObject usuario, String ficheros) 
			throws GestionEniException {

		//preparamos los ficheros json
		String indice;
		JSONObject ficherosJson;
		try {
			ficherosJson = new JSONObject(ficheros.trim());
			//Tomamos el contenido del indice
			indice = ficherosJson.getString(GestionEniConstantes.INDEX_XML);
		} catch( JSONException e) {
			throw new GestionEniException(CodigoMensajeEni.ERROR_LECTURA_JSON_ZIP.getCodigo(), 
					"Ha habido un error en la lectura del zip", e, "{}");
		}
		
		if (indice != null) {
			GestionEniExpediente expedienteEni = new GestionEniExpediente();
			//Obtenemos el objeto Expediente a partir del indice
			Expediente exp = GestionEniConversor.importarExpedienteEniByString(indice);
			//Comprobamos que todos los documentos que indica el indice se encuentran entre los ficheros
			List<DocumentoIndizado> documentos = exp.getIndice().getContenido().getDocumentos();
			List<String> archivos = obtenerClaves(ficherosJson); 
			try {
				GestionEniConversor.comprobarDocumentos(archivos, documentos);
				return expedienteEni.importExpediente(exp, ficherosJson, usuario);
			} catch (DocumentNotFoundException ex) {
				throw new GestionEniException(CodigoMensajeEni.ERROR_LECTURA_ZIP_DOCUMENTO.getCodigo(),
					"No se ha encontrado algun documento", ex);
			} catch (GestionEniException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new GestionEniException(
						CodigoMensajeEni.ERROR_EXCEPTION_LECTURA.getCodigo(), "Ha habido un error", ex);
			}
		}
		return null;	
	}
	
	/**
	 * Metodo para guardar un historico de las operaciones de exporatacion e importacion
	 *
	 * @param tipoOp Enumerado que indica el tipo de operacion
	 * @param usuario Informacion de la sesion del usuario que realiza el registro
	 * @param numExpediente
	 * @param params parametros de conexion en base de datos
	 * @throws es.altia.common.exception.TechnicalException
	 */
	public void guardarRegistroExp(TiposRegistro tipoOp, UsuarioValueObject usuario, String numExpediente, String[] params) throws TechnicalException {
		OperacionesExpedienteManager.getInstance().registrarOperacionExpediente(tipoOp, usuario, numExpediente, params);
	}

	/**
	 * Método utilizado para eliminar los directoriosTemporales que se utilizan
	 * en la importación y exportación
	 *
	 * @param dir DirectorioTemporal
	 */
	public void eliminarDirectorioTemporal(DirectorioTemporal dir) {        
		//primero se eliminan todos los arcchivos del directorio para que quede vacio
		for (String arch : dir.getArchivos()) {
			File eliminado = new File(dir.getPath(), arch);
			eliminado.delete();
		}
		//se borra el directorio
		File directorioABorrar = new File(dir.getPath());
		directorioABorrar.delete();
	}

}
