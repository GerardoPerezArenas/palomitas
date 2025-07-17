/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni.util;

import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.eni.conversoreni.ConversorEni;
import es.altia.eni.conversoreni.ConversorEniException;
import es.altia.eni.conversoreni.conversor.TipoConversion;
import es.altia.eni.conversoreni.domain.Documento;
import es.altia.eni.conversoreni.domain.DocumentoIndizado;
import es.altia.eni.conversoreni.domain.Expediente;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.util.ParserUtils;
import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin
 */
public class GestionEniConversor {

	private static final Logger LOGGER = Logger.getLogger(GestionEniConversor.class);

//[EXPORTAR]-------------------------------------------------------------------
	/**
	 * Metodo principal de la exportacion a XML. E el metodo encargado de llamar
	 * a los correspondientes metodos que realizan la conversion
	 *
	 * @param expedienteEni Expediente Eni que queremos transformar
	 * @return
	 */         
	public static File expedienteEniToFicheroXML(Expediente expedienteEni, String temporal) throws ConversorEniException {
		try {
			return new File(convertirByteArrayToFileXml(expedienteEniToByteArray(expedienteEni),temporal, GestionEniConstantes.INDEX_XML));
		} catch (ConversorEniException ex) {
			LOGGER.error("Error al convertir el expediente a xml: ", ex);
			throw (ex);
		}
	}

	/**
	 * Metodo para convertir un expediente en un ByteArrayOutputStream
	 *
	 * @param expedienteEni
	 * @return
	 * @throws ConversorEniException
	 */
	private static ByteArrayOutputStream expedienteEniToByteArray(Expediente expedienteEni) throws ConversorEniException {
		ByteArrayOutputStream xmlGenerado = new ByteArrayOutputStream();
		ConversorEni conversorEni = new ConversorEni();
		conversorEni.generar(expedienteEni, xmlGenerado, TipoConversion.XML);
                return xmlGenerado;
	}

	/**
	 * Metodo principal de la exportacion a XML. E el metodo encargado de llamar
	 * a los correspondientes metodos que realizan la conversion
	 *
	 * @param documentoEni Documento Eni que queremos transformar
         * @param carpetaTemporal directorio temporal donde se crearan los archivos XML
	 * @return
	 */
	public static File documentoEniToFicheroXML(Documento documentoEni, String carpetaTemporal) {
		try {
			String nombreFichero = String.format(
					"%s%s", documentoEni.getMetadatosEni().getIdentificador(), ConstantesDatos.EXTENSION_FICHERO_XML_CON_PUNTO);
			return new File(convertirByteArrayToFileXml(documentoEniToByteArray(documentoEni), carpetaTemporal, nombreFichero));
		} catch (ConversorEniException ex) {
			LOGGER.error("Error al convertir el documentoEni a xml: ", ex);
		}
		return null;
	}

	/**
	 * Metodo para convertir un Documento en un ByteArrayOutputStream
	 *
	 * @param documentoEni
	 * @return
	 * @throws ConversorEniException
	 */
	private static ByteArrayOutputStream documentoEniToByteArray(Documento documentoEni) throws ConversorEniException {
		ByteArrayOutputStream xmlGenerado = new ByteArrayOutputStream();
		ConversorEni conversorEni = new ConversorEni();
		conversorEni.generar(documentoEni, xmlGenerado, TipoConversion.XML);
		return xmlGenerado;
	}

	/**
	 * Metodo para tranformar un ByteArrayOutputStream en un fichero xml
	 *
	 * @param byteArrayOutputStream
	 * @param carpetaTemporal
	 * @param nombreFichero
	 * @return
	 */
	private static String convertirByteArrayToFileXml(ByteArrayOutputStream byteArrayOutputStream, String carpetaTemporal, String nombreFichero) {
		OutputStream outputStream = null;
		try {
			String pathExportEni = ResourceBundle.getBundle("resources/paths").getString(GestionEniConstantes.PATH_EXPORT_ENI);
			
			String path = String.format("%s\\%s\\", pathExportEni, carpetaTemporal);
			File temporal = new File(path);
			temporal.mkdirs();
			String rutaCompleta = String.format("%s%s", path, nombreFichero);
			outputStream = new FileOutputStream(rutaCompleta);
			byteArrayOutputStream.writeTo(outputStream);
			byteArrayOutputStream.close();
			outputStream.close();
			return rutaCompleta;

		} catch (FileNotFoundException ex) {
			LOGGER.error("Error al convertir a xml: ", ex);
		} catch (IOException ex) {
			LOGGER.error("Error al convertir a xml: ", ex);
		}

		return null;
	}

//[IMPORTAR]--------------------------------------------------------------------
    /**
     * Metodo para convertir en xml en un Documento Eni
     *
     * @param docu	Documento ENI en formato XML
     * @return
	 * @throws es.altia.flexia.eni.exception.GestionEniException
	 * 
	 */
	public static Documento importarDocumentoEniByString (String docu) throws GestionEniException {
		Documento documentoLeido = null;
		try {
			ConversorEni conversorEni = new ConversorEni();
			InputStream ficheroEntrada = new ByteArrayInputStream(docu.getBytes());
			documentoLeido = conversorEni.lecturaDocumento(ficheroEntrada, TipoConversion.XML);
		}catch (ConversorEniException ex) {
			LOGGER.error("Error al convertir el documentoEni a xml: ", ex);
			throw new GestionEniException(CodigoMensajeEni.ERROR_LECTURA_DOCUMENTO_ENI.getCodigo(), 
					"Error al convertir el XML a Documento");
		}
		return documentoLeido;
	}
        
	/**
	 * Método para comprobar si hay algun documento incluido en el indice que no se encuentre en el zip de importación.
	 * Lanzará un DocuementNotFoundExcepcion con un mensaje especificando los documentos que no se han encontrado
	 * 
	 * @param ficheros
	 * @param documentos
	 * @throws DocumentNotFoundException 
	 *
	 */
	public static void comprobarDocumentos(List<String> ficheros, List<DocumentoIndizado> documentos) throws DocumentNotFoundException {
		StringBuilder sb = new StringBuilder();
		for (DocumentoIndizado doc : documentos) {
			if (!ficheros.contains(doc.getIdDocumento() + ".xml")) {
				sb.append(doc.getIdDocumento()).append(" : no encontrado\n");
			}
		}
		if (sb.length() != 0) {
			LOGGER.error(sb.toString());
			throw new DocumentNotFoundException(sb.toString());
		}
	}

	/**
	 * Convierte a expediente un archivo XML hecho String
	 * @param file
	 * @return 
	 */
public static Expediente importarExpedienteEniByString(String file){
		Expediente expedienteLeido = null;
		try{	
			ConversorEni conversorEni = new ConversorEni();
			InputStream ficheroIndice = new ByteArrayInputStream(file.getBytes());
			expedienteLeido = conversorEni.lecturaExpediente(ficheroIndice, TipoConversion.XML);
		} catch (ConversorEniException ex) {
			LOGGER.error("Error al convertir el documentoEni a xml: ", ex);
		}
		return expedienteLeido;
	}
		
	/**
	 * Convierte una lista de {@link TercerosValueObject} en una lista de {@link InteresadoExpedienteVO} utilizando la 
	 * informacion necesaria del {@link ExpedienteVO}.
	 * 
	 * @param expediente
	 * @param terceros
	 * @return 
	 */
	public static List<InteresadoExpedienteVO> listaTercerosValueObjectToListaInteresadosExpedienteVO(
			ExpedienteVO expediente, List<TercerosValueObject> terceros) {
		
		List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();
		for (TercerosValueObject tercero : terceros) {
			InteresadoExpedienteVO interesado = tercerosValueObjectToInteresadoExpediente(expediente, tercero);
			interesados.add(interesado);
		}
		return interesados;
	}

	/**
	 * Convierte un {@link TercerosValueObject} en un {@link InteresadoExpedienteVO} utilizando la informacion necesaria
	 * del {@link ExpedienteVO}.
	 * 
	 * @param expediente Expediente con el que esta relacionado el tercero. No puede ser null.
	 * @param tercero Tercero a relacionar con el expediente como interesado. No puede ser null.
	 * @return 
	 */
	private static InteresadoExpedienteVO tercerosValueObjectToInteresadoExpediente(ExpedienteVO expediente, TercerosValueObject tercero) {
		InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
		
		interesado.setCodDomicilio(ParserUtils.parsear(tercero.getDomPrincipal(), 0));
		interesado.setCodMunicipio(expediente.getCodOrganizacion());
		interesado.setCodProcedimiento(expediente.getCodProcedimiento());
		interesado.setCodRol(tercero.getCodRol());
		interesado.setCodTercero(ParserUtils.parsear(tercero.getCodTerceroOrigen(), 0));
		interesado.setEjercicio(expediente.getEjercicio());
		interesado.setIdProceso(expediente.getIdProceso());
		interesado.setMostrar(1);
		interesado.setNotificacionElectronica("0"); // Tener el mail como dato no implica tener consentimiento de notificacion
		interesado.setNumExpediente(expediente.getNumExpediente());
		interesado.setVersionTercero(ParserUtils.parsear(tercero.getVersion(), 0));
		
		return interesado;
	}
	
//[GESTION ZIP]-----------------------------------------------------------------
	public static String comprimirFilesZip(String directorio, DirectorioTemporal dirTemp, String gzipFile) throws IOException {
		ZipOutputStream zous = null;
		FileInputStream fis = null;

		String pathFileZip = String.format("%s%s", directorio, gzipFile);
		try {
			zous = new ZipOutputStream(new FileOutputStream(pathFileZip));
			String temporal = dirTemp.getPath();
			for (String string : dirTemp.getArchivos()) {
				try {
					ZipEntry entrada = new ZipEntry(string);
					zous.putNextEntry(entrada);
					String dir = temporal + entrada.getName();
					fis = new FileInputStream(dir);
					int leer;
					byte[] buffer = new byte[1024];
					while (0 < (leer = fis.read(buffer))) {
						zous.write(buffer, 0, leer);
					}
					}catch(NullPointerException ex){
						ex.printStackTrace();
					}catch(Exception ex){
						ex.printStackTrace();
					}finally{
						if (fis != null) {
							fis.close();
						} 
						if (zous != null) {
							zous.closeEntry(); 
						}

					}
				}
			return pathFileZip;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zous != null) {
				zous.close();
			}
		}
		return null;
	}

	public static DirectorioTemporal descomprimirZip(String directorio) throws IOException {
		DirectorioTemporal dirTemp = new DirectorioTemporal();
		String extension = "";
		String directorioTemporal = "";
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		//cadena que contiene la ruta donde est?los archivos .zip
		int endIndex = directorio.lastIndexOf("\\") + 1;
		String path = directorio.substring(0, endIndex);
		
		//nombre del .zip a extraer
		String archivo = directorio.substring(endIndex, directorio.length());

		//Creacion del File que contiene el archivo
		File archivoAux = new File(directorio);

		//Se comprueba que existe el archivo
		if (archivoAux.exists()) {
			int extensionFicheroIndice = archivoAux.getName().lastIndexOf('.');
			if (extensionFicheroIndice > 0) {
				extension = archivoAux.getName().substring(extensionFicheroIndice + 1);
			}
			if (extension.equals("zip")) {
				LOGGER.debug(String.format("Descomprimiendo fichero: %s", archivoAux.getName()));
				try {
					//crea un buffer temporal para el archivo que se va descomprimir
					zis = new ZipInputStream(new FileInputStream(directorio));
					ZipEntry salida;
					//Creamos un directorio temporal con el nombre del archivo donde se descomprimira
					directorioTemporal = path + archivo.substring(0, archivo.lastIndexOf('.')) + "\\";
					new File(directorioTemporal).mkdir();
					
					dirTemp.setPath(directorioTemporal);
					List<String> archivos = new ArrayList<String>();
					while (null != (salida = zis.getNextEntry())) {
						LOGGER.debug(String.format("Nombre del Archivo: %s", salida.getName()));
						fos = new FileOutputStream(directorioTemporal + salida.getName());
						int leer;
						byte[] buffer = new byte[1024];
						while (0 < (leer = zis.read(buffer))) {
							fos.write(buffer, 0, leer);
						}
						fos.close();
						zis.closeEntry();
						archivos.add(salida.getName());
					}
					zis.close();
					dirTemp.setArchivos(archivos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					fos.close();
					zis.close();
				}
			}

			return dirTemp;

		} else {
			LOGGER.debug("No se encontro directorio");
			return null;
		}
	}
	
	/**
	 * Metodo que rellena un String con la cantidad de ceros necesarios.
	 * @param cadenaSinFormato Cadena que necesitamos rellenar con ceros
	 * @param longitud longitud que necesitamos rellenar con ceros
	 * @return 
	 * @throws GestionEniException
	 */
	public static String formateadorCeros(String cadenaSinFormato, int longitud) throws GestionEniException{
		String cadenaSinEspacios = cadenaSinFormato.trim();
		// Cantidad de 0's a insertar antes del codigo
		int ceros = longitud - cadenaSinEspacios.length();
		if (ceros < 0) {
			// Este caso nunca se deberia dar, dado que dañaria la integridad del codigo de la unidad organica o el formato ENI
			String mensaje = String.format(
					"El codigo de unidad organica (%s) supera la cantidad de caracteres maxima (%d)", 
					cadenaSinEspacios, longitud);
			throw new GestionEniException(CodigoMensajeEni.ERROR_AL_RECUPERAR_UNIDADES_ORGANICAR.getCodigo(), mensaje, 
					mensaje);
		}
		StringBuilder cadenaResultado = new StringBuilder();
		for (int i = 0; i < ceros; i++) {
			// Solo ocurre en caso de que ceros > 0
			cadenaResultado.append("0");
		}
		return cadenaResultado.append(cadenaSinEspacios).toString();
	}
	
	public static String formateadorNombrefichero(String nombreFichero, String Extension){
		return String.format("%s_%s.%s", GestionEniConstantes.PROCEDIMIENTO_ENI, nombreFichero.split("_")[3], Extension);
	}
	

}
