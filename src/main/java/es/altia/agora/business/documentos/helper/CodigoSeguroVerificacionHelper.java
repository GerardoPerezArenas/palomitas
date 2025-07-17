package es.altia.agora.business.documentos.helper;

import com.itextpdf.text.DocumentException;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.manual.DocumentoDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.ByteArrayInOutStream;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.pdf.PdfHelper;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CodigoSeguroVerificacionHelper {
    
    private static final String CONFIG_DOC_CSV_URI_SERVLET_VER_DOCUMENTO = "CSV/URI/SERVLET_VER_DOCUMENTO";
    
    private static final Config configDoc = ConfigServiceHelper.getConfig("documentos");
    private static final Log log = LogFactory.getLog(CodigoSeguroVerificacionHelper.class.getName());

    /**
     * Genera un codigo seguro de verificacion. El codigo se compone de 25
     * caracteres alfanumericos aleatorios y una cabecera opcional de 4
     * caracteres separada por un guion.
     *
     * Ejemplos
     * Con cabecera: CBCR-5BEHCL4BW8UHC43AVVL0E63GW
     * Sin cabecera: 2O2PDGODHX2PL55730D26PW9J
     *
     * @param cabecera
     * @param semilla
     * @return
     */
    public static String crearCodigoSeguroVerificacion(String cabecera, String semilla) {
        StringBuilder cadenaAleatoria = new StringBuilder();
        String csvTemporal = null;
        String csvFinal = null;

        if (log.isDebugEnabled()) {
            log.debug("crearCodigoSeguroVerificacion");
            log.debug(String.format("cabecera = %s", cabecera));
            log.debug(String.format("semilla = %s", semilla));
        }
        
        // Generacion del codigo seguro de verificacion sin cabecera
        // Se repetira el proceso hasta recibir un codigo de 25 caracteres,
        // ya que a veces se generan codigos de diferentes tamanos durante
        // la conversion en base 36
        do {
            cadenaAleatoria.setLength(0);
            // Añadimos la semilla al codigo a generar
            if (StringUtils.isNotEmpty(semilla)) {
                cadenaAleatoria.append(semilla.trim());
            }

            // Anadimos el numero aleatorio
            cadenaAleatoria.append(UUID.randomUUID());

            // Se convierte a MD5 y posteriormente a base 36 para obtener un codigo alfanumerico
            csvTemporal = new BigInteger(DigestUtils.md5Hex(cadenaAleatoria.toString()), 16)
                    .toString(36)
                    .toUpperCase();
        } while (csvTemporal.length() != 25);

        // Anadimos cabecera si fuese necesario
        if (StringUtils.isNotEmpty(cabecera)) {
            csvFinal = String.format("%s-%s", StringUtils.substring(cabecera.trim(), 0, 4), csvTemporal);
        } else {
            csvFinal = csvTemporal;
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Codigo seguro de verificacion generado: %s", csvFinal));
        }
        
        return csvFinal;
    }

    /**
     * Genera el codigo seguro de verificacion y lo incrusta en el lateral del
     * documento PDF.
     * Para ver como se genera el CSV ver el método {@link #crearCodigoSeguroVerificacion(java.lang.String, java.lang.String) }
     *
     * @param in
     * @param out
     * @param cabecera
     * @param semilla
     * @param codOrganizacion
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public static String incrustarCSVenPDF(InputStream in, OutputStream out, String cabecera, String semilla, String codOrganizacion) throws TechnicalException {
        String csv = null;
        
        ByteArrayInOutStream tempOutputStream = null;
        
        try {
            // Obtenemos las propiedades necesarias para poder incrustar el texto en el pdf
            String texto1 = configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_TEXTO1"));
            String texto2 = configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_TEXTO2"));
            float alto = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_ALTO")));
            float ancho = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_ANCHO")));
            float posXCentro = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_POS_X_CENTRO")));
            float posYCentro = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_POS_Y_CENTRO")));
            float rotacion = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_ROTACION")));
            float offsetTextoX = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_OFFSET_TEXTO_X")));
            float tamanoFuente = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_TAMANO_FUENTE")));
            boolean superpuesto = configDoc.getBoolean(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_SUPERPUESTO"));
            boolean pintarBorde = configDoc.getBoolean(String.format("%s%s", codOrganizacion, "/CSV/RECUADRO_PINTAR_BORDE"));
            float escalado = Float.parseFloat(configDoc.getString(String.format("%s%s", codOrganizacion, "/CSV/ESCALADO")));

            // Generamos el CSV
            csv = crearCodigoSeguroVerificacion(cabecera, semilla);
            
            // Realizamos un escalado del contenido del documento para hacer sitio
            // para incrustar el texto lateral
            tempOutputStream = new ByteArrayInOutStream();
            PdfHelper.ajustarPaginaPDF(in, tempOutputStream, escalado);

            // Incrustamos el codigo en el pdf
            String textoCSV = texto1.replace("@csv@", csv);
            PdfHelper.incrustarTextoLateralPDF(
                    tempOutputStream.getInputStream(), out,
                    Arrays.asList(textoCSV,texto2), alto, ancho,
                    posXCentro, posYCentro, rotacion, offsetTextoX,
                    tamanoFuente, superpuesto, pintarBorde);
        } catch (DocumentException de) {
            csv = null;
            throw new TechnicalException("No se ha podido incrustar el CSV en el pdf", de);
        } catch (Exception e) {
            csv = null;
            throw new TechnicalException("Ha ocurrido un fallo al intentar generar e incrustar el CSV en el pdf", e);
        } finally {
            IOUtils.closeQuietly(tempOutputStream);
        }

        return csv;
    }

    /**
     * Obtiene los metadatos de un documento
     *
     * @param idMetadatos identificador del metadato
     * @param params parametros de conexion a base de datos
     * @return el valor de los metadatos
     * @throws es.altia.common.exception.TechnicalException
     */
    public static MetadatosDocumentoVO getMetadatos(Long idMetadatos, String[] params) throws TechnicalException {
        MetadatosDocumentoVO metadatos = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            metadatos = DocumentoDAO.getInstance().getMetadatos(idMetadatos, con, params);
        } catch (BDException bde) {
            throw new TechnicalException("Error al intentar obtener los metadatos de base de datos", bde);
        } catch (Exception e) {
            throw new TechnicalException("Error desconocido al intentar obtener los metadatos", e);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return metadatos;
    }
    
    /**
     * Inserta los metadatos de un documento en base de datos.
     *
     * @param metadato objeto que contiene los datos del objeto a dar de alta
     * @param params parametros de conexion a base de datos
     * @return identificador en base de datos del metadato
     * @throws es.altia.common.exception.TechnicalException
     */
    public static Long insertarMetadatoCSV(MetadatosDocumentoVO metadato, String[] params) throws TechnicalException {
        Long id = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            id = DocumentoDAO.getInstance().insertarMetadatoCSV(metadato, con, params);

            SigpGeneralOperations.commit(adapt, con);
        } catch (BDException bde) {
            log.error("Error al intentar insertar el metadato en base de datos", bde);
            SigpGeneralOperations.rollBack(adapt, con);
        } catch (TechnicalException te) {
            log.error("Error al intentar insertar el metadato", te);
            SigpGeneralOperations.rollBack(adapt, con);
        } catch (Exception e) {
            log.error("Error desconocido al intentar insertar el metadato", e);
            SigpGeneralOperations.rollBack(adapt, con);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return id;
    }

    /**
     * Elimina los metadatos de un documento en base de datos.
     *
     * @param id identificador del metadato
     * @param params parametros de conexion a base de datos
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
    public static boolean eliminarMetadato(Long id, String[] params) throws TechnicalException {
        boolean correcto = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            DocumentoDAO.getInstance().eliminarMetadato(id, con);

            SigpGeneralOperations.commit(adapt, con);

            correcto = true;
        } catch (BDException bde) {
            log.error("Error al intentar eliminar el metadato en base de datos", bde);
            SigpGeneralOperations.rollBack(adapt, con);
        } catch (TechnicalException te) {
            log.error("Error al intentar eliminar el metadato", te);
            SigpGeneralOperations.rollBack(adapt, con);
        } catch (Exception e) {
            log.error("Error desconocido al eliminar insertar el metadato", e);
            SigpGeneralOperations.rollBack(adapt, con);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return correcto;
    }
    
    /**
     * Busca si esta informado el campo CSV para los metadatos de un documento 
     * en base de datos. Se le debe pasar una subconsulta que retorna el ID
     * del metadato a borrar y los parametros que necesite dicha subconsulta.
     * 
     * Ejemplo:
     * subSelect: SELECT ID_METADATO FROM E_DOC_EXT WHERE DOC_EXT_COD=?
     * paramSubSelect: paramSubSelect[0] = 1
     *
     * @param params parametros de conexion a base de datos
     * @param subSelect subconsulta para obtener el id del metadato
     * @param paramSubSelect argumentos para la subconsulta
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
    public static boolean existeMetadatoCSV(String[] params, String subSelect, Object... paramSubSelect) throws TechnicalException {
        boolean existe = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            existe = DocumentoDAO.getInstance().existeMetadatoCSV(subSelect, paramSubSelect, con);
        } catch (BDException bde) {
            log.error("Error al intentar determinar si existe el metadato en base de datos", bde);
            throw new TechnicalException("Error al intentar determinar si existe el metadato en base de datos", bde);
        } catch (TechnicalException te) {
            log.error("Error al intentar determinar si existe el metadato en base de datos", te);
            throw new TechnicalException("Error al intentar determinar si existe el metadato en base de datos", te);
        } catch (Exception e) {
            log.error("Error desconocido al intentar determinar si existe el metadato", e);
            throw new TechnicalException("Error desconocido al intentar determinar si existe el metadato", e);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return existe;
    }

    /**
     * Genera una URL con la llamada al servlet de recuperacion del documento 
     * pasandole como parametro los valores del mapa.
     * 
     * @param params Parametros para componer la URL (clave=valor)
     * @return 
     */
    public static String crearURLCodigoSeguroVerificacion(Map<String, Object> params) {
        StringBuilder url = new StringBuilder();

        log.debug("crearURLCodigoSeguroVerificacion");
        
        url.append(configDoc.getString(CONFIG_DOC_CSV_URI_SERVLET_VER_DOCUMENTO));
        url.append(generarParametrosGet(params));
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("URL GENERADA: %s", url.toString()));
        }
        
        return url.toString();
    }

	/**
	 * Método que obtiene de Registro.properties la propiedad INCRUSTAR_CSV_JUSTIFICANTE_REGISTRO
	 * @param organizacion Codigo de organizacion del usuario
	 * @param registroConf Fichero properties del que obtener la propiedad
	 * @return True o False si la propiedad está activa o no
	 */
	public static boolean incrustarCSVenJustificante(int organizacion, Config registroConf){
		String propiedad = organizacion + "/INCRUSTAR_CSV_JUSTIFICANTE_REGISTRO";
		String valor = null;
		
		try{
			valor = registroConf.getString(propiedad);
			log.debug("Se recupera el valor '" + valor + "' de la propiedad '" + propiedad + "' de Registro.properties");
		} catch(Exception e){
			log.error("Se ha producido un error recuperando la propiedad '" + propiedad + "' de Registro.properties");
		}
		
		if(valor != null && valor.equalsIgnoreCase("SI"))
			return true;
		else
			return false;
	}
    
	/**
	 * Método que obtiene de Registro.properties la propiedad INCRUSTAR_CSV_JUSTIFICANTE_REGISTRO
	 * @param organizacion Codigo de organizacion del usuario
	 * @return True o False si la propiedad está activa o no
	 */
	public static boolean incrustarCSVenJustificante(int organizacion){
		Config registroConf = ConfigServiceHelper.getConfig("Registro");
		
		return incrustarCSVenJustificante(organizacion, registroConf);
	}
    
    /**
     * Genera la parte de los parametros de una URL tipo GET
     * Ej. ?clave1=valor1&clave2=valor2
     * @param params
     * @return 
     */
    private static String generarParametrosGet(Map<String, Object> params) {
        StringBuilder paramString = new StringBuilder();

        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {

                if (paramString.length() != 0) {
                    paramString.append('&');
                } else {
                    paramString.append("?");
                }
                
                paramString.append(param.getKey());
                paramString.append('=');
                paramString.append(String.valueOf(param.getValue()));
            }
        }
        
        return paramString.toString();
    }
}