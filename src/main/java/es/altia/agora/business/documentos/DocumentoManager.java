package es.altia.agora.business.documentos;

import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.CSVPendienteVO;
import es.altia.agora.business.sge.persistence.manual.DocumentoDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.util.security.dao.SeguridadDAO;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentoManager {

    private static DocumentoManager instance = null;
    
    private SeguridadDAO seguridadDao;
    private DocumentoDAO documentDao;
    
    private static final Log log = LogFactory.getLog(DocumentoManager.class.getName());

    private DocumentoManager() {
        seguridadDao = SeguridadDAO.getInstance();
        documentDao = DocumentoDAO.getInstance();
    }

    /**
     * Factory method para el <code>Singelton</code>.
     *
     * @return La unica instancia de DocumentoManager
     */
    public static DocumentoManager getInstance() {
        synchronized (DocumentoManager.class) {
            if (instance == null) {
                instance = new DocumentoManager();
            }
        }
        return instance;
    }

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
    public String crearCodigoSeguroVerificacion(String cabecera, String semilla) {

        return CodigoSeguroVerificacionHelper.crearCodigoSeguroVerificacion(cabecera, semilla);
    }

    /**
     * Genera una URL con la llamada al servlet de recuperacion del documento 
     * pasandole como parametro los valores del mapa.
     * 
     * @param params Parametros para componer la URL (&clave=valor)
     * @return 
     */
    public String crearURLCodigoSeguroVerificacion(Map<String, Object> params) {

        return CodigoSeguroVerificacionHelper.crearURLCodigoSeguroVerificacion(params);
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
    public String incrustarCSVenPDF(InputStream in, OutputStream out, String cabecera,
            String semilla, String codOrganizacion)
            throws TechnicalException {
        
        return CodigoSeguroVerificacionHelper.incrustarCSVenPDF(in, out, cabecera, semilla, codOrganizacion);
    }

    /**
     * Inserta los metadatos de un documento en base de datos.
     *
     * @param metadato objeto que contiene los datos del objeto a dar de alta
     * @param params parametros de conexion a base de datos
     * @return identificador en base de datos del metadato
     * @throws es.altia.common.exception.TechnicalException
     */
    public Long insertarMetadatoCSV(MetadatosDocumentoVO metadato, String[] params) throws TechnicalException {

        return CodigoSeguroVerificacionHelper.insertarMetadatoCSV(metadato, params);
    }

    /**
     * Elimina los metadatos de un documento en base de datos.
     *
     * @param id identificador del metadato
     * @param params parametros de conexion a base de datos
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean eliminarMetadato(Long id, String[] params) throws TechnicalException {

        return CodigoSeguroVerificacionHelper.eliminarMetadato(id, params);
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
    public boolean existeMetadatoCSV(String[] params, String subSelect, Object... paramSubSelect) throws TechnicalException {

        return CodigoSeguroVerificacionHelper.existeMetadatoCSV(params, subSelect, paramSubSelect);
    }
    
    /**
     * Obtiene los metadatos de un documento
     *
     * @param idMetadato identificador del metadato
     * @param params parametros de conexion a base de datos
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
    public MetadatosDocumentoVO getMetadatos(Long idMetadato, String[] params) throws TechnicalException {

        return CodigoSeguroVerificacionHelper.getMetadatos(idMetadato, params);
    }
    
    /**
     * Inserta el csv pendiente de procesar
     *
     * @param datos objeto que contiene los datos del objeto a dar de alta
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void insertarCsvPendienteProcesar(CSVPendienteVO datos, Connection conexion) throws TechnicalException {
        documentDao.insertarCsvPendienteProcesar(datos, conexion);
    }
    
    /**
     * Comprubea si el csv pendiente de procesar y el token se corres
     *
     * @param token
     * @param csv
     * @param conexion conexion de base de datos
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean existeCsvPendienteProcesar(String token, String csv, Connection conexion)
            throws TechnicalException {
        return documentDao.existeCsvPendienteProcesar(token, csv, conexion);
    }

    /**
     * Eliminación del registro de CSV pendiente de procesar y del token
     * asociado
     * 
     * @param token
     * @param csv
     * @param conexion
     * @throws TechnicalException 
     */
    public void eliminarCsvPendienteProcesarToken(String token, String csv, Connection conexion)
            throws TechnicalException {
        
        log.debug("eliminarCsvPendienteProcesar");

        log.debug("Se obtiene el id del token");
        Long idToken = seguridadDao.obtenerIdToken(token, conexion);
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("idToken obtenido: %d", idToken));
        }
        
        if (idToken != null) {
            log.debug("Se elimina el CSV pendiente de procesar");
            documentDao.eliminarCsvPendienteProcesar(idToken, csv, conexion);

            log.debug("Se elimina el token de autenticacion");
            seguridadDao.eliminarTokenExternos(idToken, conexion);
        }
    }
}
