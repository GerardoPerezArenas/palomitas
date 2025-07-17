package es.altia.flexia.portafirmasexternocliente.plugin.persistance;

import es.altia.agora.business.sge.firma.FirmaFlujoManager;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramitacionVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.sge.firma.vo.FirmaFirmanteVO;
import es.altia.agora.business.sge.firma.vo.FirmaTipoVO;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.flexia.portafirmasexternocliente.exception.PortafirmasExternoException;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.manual.PluginPortafirmasExternoClienteDao;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.SanseUtils;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.ConstantesPortafirmasSanse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Request;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignLine;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signer;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PluginPortafirmasExternoClienteSanseManager {

    //Logger
    private static final Log log = LogFactory.getLog(PluginPortafirmasExternoClienteSanseManager.class.getName());

    //Instance
    private static PluginPortafirmasExternoClienteSanseManager instance = null;

    // Managers
    private static PluginPortafirmasExternoClienteManager pluginCommonManager;
    private static DocumentosExpedienteManager documentoManager;
    private static FirmaFlujoManager firmaFlujoManager;

    // DAOs
    private static PluginPortafirmasExternoClienteDao pluginCommonDao;
    private static TramitacionExpedientesDAO tramitacionExpedientesDao;

    // Mensajes de error
    private static final String ERROR_OBTENER_DATOS_CREACION_PETICION
            = "Ha ocurrido un error al intentar obtener los datos para la creacion de la peticion en el portafirmas de SANSE";
    private static final String ERROR_OBTENER_DATOS_INSERCION_FIRMANTES
            = "Ha ocurrido un error al intentar obtener los datos para la insercion de firmantes en el portafirmas de SANSE";
    private static final String ERROR_OBTENER_DATOS_INSERCION_DOCUMENTOS
            = "Ha ocurrido un error al intentar obtener los datos para la insercion del documento en el portafirmas de SANSE";
    private static final String ERROR_OBTENER_LISTADO_FIRMANTES_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de usuarios firmantes";
    private static final String ERROR_OBTENER_TIPO_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el tipo de firma del documento";
    private static final String ERROR_ACTUALIZAR_ESTADO_FIRMAS_BBDD
            = "Ha ocurrido un error de bbdd al intentar actualizar los estados de las firmas";
    
    private PluginPortafirmasExternoClienteSanseManager() {
        pluginCommonManager = PluginPortafirmasExternoClienteManager.getInstance();
        pluginCommonDao = PluginPortafirmasExternoClienteDao.getInstance();
        firmaFlujoManager = FirmaFlujoManager.getInstance();
        documentoManager = DocumentosExpedienteManager.getInstance();
        tramitacionExpedientesDao = TramitacionExpedientesDAO.getInstance();
    }

    /**
     * Recupera una instancia (Singleton) de la clase
     *
     * @return PluginPortafirmasExternoClienteSanseManager
     */
    public static PluginPortafirmasExternoClienteSanseManager getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }

        PluginPortafirmasExternoClienteSanseManager temp = instance;
        if (temp == null) {
            synchronized (PluginPortafirmasExternoClienteSanseManager.class) {
                temp = instance;
                if (temp == null) {
                    temp = new PluginPortafirmasExternoClienteSanseManager();
                    instance = temp;
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }

        return instance;
    }

    /**
     * Obtiene todos los datos necesarios para la creacion de la peticion en el
     * portafirmas de SANSE
     *
     * @param codOrg
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTram
     * @param ocuTram
     * @param codDoc
     * @param params
     * @param datos
     * @throws PortafirmasExternoException
     */
    public void obtenerDatosCreacionPeticion(
            Integer codOrg, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTram, Integer ocuTram,
            Integer codDoc, String[] params, GeneralValueObject datos)
            throws PortafirmasExternoException {

        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        log.debug("obtenerDatosCreacionPeticion");
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String codOrganizacion = Integer.toString(codOrg);
            String idRemitente = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_ID_REMITENTE);
            String aplicacion = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_APLICACION);
            Boolean selloDeTiempo = SanseUtils.getPropiedadSanseBoolean(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_SELLO_TIEMPO);
            Integer diasExpiracion = SanseUtils.getPropiedadSanseInt(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_FECHA_EXPIRACION);
            String nivelImportanciaId = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_NIVEL_IMPORTANCIA_ID);
            String nivelImportanciaDesc = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_NIVEL_IMPORTANCIA_DESC);
            
            Calendar fechaInicio = Calendar.getInstance();
            Calendar fechaExpiracion = (Calendar) fechaInicio.clone();
            fechaExpiracion.add(Calendar.DAY_OF_YEAR, diasExpiracion);
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("fechaInicio=%s", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaInicio.getTime())));
                log.debug(String.format("fechaExpiracion=%s", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaInicio.getTime())));
            }
            
            FirmaTipoVO tipoFirma = getTipoFirmaTramite(
                    codOrg, codProcedimiento, ejercicio,
                    numExpediente, codTram, ocuTram, codDoc,
                    params);
            
            // Si no tiene especificado tipo de firma es que no es un flujo,
            // por lo que se envía en modo CASCADA
            if (tipoFirma == null) {
                tipoFirma = firmaFlujoManager.getTipoFirma(
                        ConstantesPortafirmas.ID_TIPO_FLUJO_CASCADA, params);
            }
            
            // Obtener el asunto y el texto con los datos sustituidos
            getAsuntoYTexto(codOrg, codProcedimiento,
                    ejercicio, numExpediente, codTram,
                    ocuTram, codDoc, datos, con);
            
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_ID_REMITENTE, idRemitente);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_REFERENCIA, numExpediente);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_APLICACION, aplicacion);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_SELLO_TIEMPO, selloDeTiempo);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FECHA_INICIO, fechaInicio);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FECHA_EXPIRACION, fechaExpiracion);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_NIVEL_IMPORTANCIA_ID, nivelImportanciaId);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_NIVEL_IMPORTANCIA_DESCRIPCION, nivelImportanciaDesc);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_FIRMA, tipoFirma.getNombre());
        } catch (PortafirmasExternoException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PortafirmasExternoException(ERROR_OBTENER_DATOS_CREACION_PETICION, e);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene todos los datos necesarios para la insercion de los firmantes en
     * el portafirmas de SANSE
     *
     * @param codOrg
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTram
     * @param ocuTram
     * @param codDoc
     * @param params
     * @param datos
     * @throws PortafirmasExternoException
     */
    public void obtenerDatosInsertarFirmantes(
            Integer codOrg, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTram, Integer ocuTram,
            Integer codDoc, String[] params, GeneralValueObject datos)
            throws PortafirmasExternoException {

        try {
            List<FirmaCircuitoVO> listaFirmantes = obtenerListadoFirmantesTramite(
                    codOrg, codProcedimiento, ejercicio,
                    numExpediente, codTram, ocuTram, codDoc,
                    params);

            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FIRMANTES, listaFirmantes);
        } catch (PortafirmasExternoException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PortafirmasExternoException(ERROR_OBTENER_DATOS_INSERCION_FIRMANTES, e);
        }
    }

    /**
     * Obtiene todos los datos necesarios para la insercion de los documentos en
     * el portafirmas de SANSE
     *
     * @param codOrg
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTram
     * @param ocuTram
     * @param codDoc
     * @param params
     * @param datos
     * @throws PortafirmasExternoException
     */
    public void obtenerDatosInsertarDocumento(
            Integer codOrg, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTram, Integer ocuTram,
            Integer codDoc, String[] params, GeneralValueObject datos)
            throws PortafirmasExternoException {

        try {
            String codOrganizacion = Integer.toString(codOrg);

            String tipoDocumentoId = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_TIPO_DOCUMENTO_ID);
            String tipoDocumentoDescripcion = SanseUtils.getPropiedadSanse(
                    codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_TIPO_DOCUMENTO_DESCRIPCION);

            DocumentoTramitacionVO documento = pluginCommonManager.getDocumentoTramitacion(
                    Integer.toString(codOrg), codProcedimiento, numExpediente, Integer.toString(codTram),
                    Integer.toString(ocuTram), Integer.toString(codDoc), params, "");

            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_DOCUMENTO_ID, tipoDocumentoId);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_DOCUMENTO_DESCRIPCION, tipoDocumentoDescripcion);
            datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_DOCUMENTO_TRAMITE, documento);
        } catch (PortafirmasExternoException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PortafirmasExternoException(ERROR_OBTENER_DATOS_INSERCION_DOCUMENTOS, e);
        }
    }

    /**
     * Obtiene el listado de firmantes de la peticion que se va a enviar al
     * servicio web de SANSE.
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param params
     * @return
     * @throws PortafirmasExternoException 
     */
    private List<FirmaCircuitoVO> obtenerListadoFirmantesTramite(
            Integer codOrganizacion, String codProcedimiento,
            Integer ejercicio, String numExpediente, Integer codTramite,
            Integer ocurrenciaTramite, Integer codDocumento, String[] params)
            throws PortafirmasExternoException {

        log.debug("obtenerListadoFirmantesTramite() : BEGIN");

        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaCircuitoVO> listaFirmantes = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaFirmantes = pluginCommonDao.getListadoFirmantesTramiteFlujoUsuarios(
                    codOrganizacion, codProcedimiento, ejercicio, numExpediente,
                    codTramite, ocurrenciaTramite, codDocumento, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new PortafirmasExternoException(ERROR_OBTENER_LISTADO_FIRMANTES_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.debug("obtenerListadoFirmantesTramite() : END");

        return listaFirmantes;
    }

    /**
     * Obtiene el tipo de firma para el documento de tramite que se va a enviar
     * al portafirmas de SANSE
     * 
     * @param codOrg
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTram
     * @param ocuTram
     * @param codDoc
     * @param params
     * @return 
     * @throws PortafirmasExternoException 
     */
    private FirmaTipoVO getTipoFirmaTramite(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer ocurrenciaTramite,
            Integer codDocumento, String[] params)
            throws PortafirmasExternoException {

        log.debug("getTipoFirmaTramite() : BEGIN");

        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaTipoVO tipoFirma = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            tipoFirma = pluginCommonDao.getTipoFirmaTramite(
                    codOrganizacion, codProcedimiento, ejercicio, numExpediente,
                    codTramite, ocurrenciaTramite, codDocumento, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new PortafirmasExternoException(ERROR_OBTENER_TIPO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.debug("getTipoFirmaTramite() : END");

        return tipoFirma;
    }

    /**
     * Compara los estados de la firma del documento con los devueltos por el
     * servicio web del portafirmas de SANSE y devuelve otro documento con
     * los estados que son necesarios actualizar.
     * Solo contiene los usuarios a actualizar con sus estados. Si no hay que
     * cambiar el estado de la firma del documento, el estado sera null.
     * 
     * @param codigoOrganizacion
     * @param doc
     * @param respuesta
     * @param params
     * @return 
     * @throws PortafirmasExternoException 
     */
    public FirmaDocumentoTramitacionVO obtenerDocumentoActualizarEstado(
            String codigoOrganizacion, FirmaDocumentoTramitacionVO doc,
            Request respuesta, String[] params)
            throws PortafirmasExternoException {

        FirmaDocumentoTramitacionVO docActualizado = null;
        
        log.debug("obtenerDocumentoActualizarEstado() : BEGIN");

        // Comprobar en que estado se encuentra la peticion
        log.debug("Recuperamos el estado de la peticion");
        String estadoPeticion = respuesta.getRequestStatus().getValue();
        String estadoFlexia = SanseUtils.mapeoEstadoFirma(estadoPeticion);
        if (log.isDebugEnabled()) {
            log.debug(String.format("Estado de la peticion: %s", estadoPeticion));
            log.debug(String.format("Estado equivalente en Flexia: %s", estadoFlexia));
        }

        // Si existe el estado se comprueba si hubo cambios en el estado de la firma
        if (StringUtils.isNotEmpty(estadoFlexia)) {
            docActualizado = new FirmaDocumentoTramitacionVO();
            docActualizado.setClave(doc.getClave());
            docActualizado.setClientePortafirmasExterno(doc.getClientePortafirmasExterno());
            docActualizado.setIdSolicitudPortafirmasExterno(doc.getIdSolicitudPortafirmasExterno());
            docActualizado.setIdDocumentoPortafirmasExterno(doc.getIdDocumentoPortafirmasExterno());
            docActualizado.setNombre(doc.getNombre());
            docActualizado.setEstadoFirma(ConstantesPortafirmas.ESTADO_FIRMA_SIN_CAMBIOS);
            
            // Si ha cambiado el estado se lo asignamos, si no queda a null para
            // determinar que no hay que actualizarlo
            if (!StringUtils.equals(estadoFlexia, doc.getEstadoFirma())) {
                docActualizado.setEstadoFirma(estadoFlexia);
                docActualizado.setFechaFirma(Calendar.getInstance());
            }
            
            SignLine[] lineaFirma = respuesta.getSignLineList();
            if (lineaFirma != null) {
                List<FirmaFirmanteVO> listaFirmantes = new ArrayList<FirmaFirmanteVO>();
                FirmaFirmanteVO firmanteActualizar = null;
                
                for (SignLine firmantesLinea : lineaFirma) {
                    if (firmantesLinea.getSignerList() == null || firmantesLinea.getSignerList().length != 1) {
                        throw new PortafirmasExternoException("El servicio web retorno una linea de firma incorrecta o no soportada por flexia");
                    }
                    
                    firmanteActualizar = obtenerFirmanteActualizarEstado(doc, firmantesLinea.getSignerList()[0]);
                    if (firmanteActualizar != null) {
                        listaFirmantes.add(firmanteActualizar);
                    }
                }
                
                docActualizado.setFirmantes(listaFirmantes);
            }
        }
        
        log.debug("obtenerDocumentoActualizarEstado() : END");
        
        return docActualizado;
    }

    private FirmaFirmanteVO obtenerFirmanteActualizarEstado(FirmaDocumentoTramitacionVO doc, Signer firmanteSanse) {
        FirmaFirmanteVO firmanteActualizar = null;
        
        List<FirmaFirmanteVO> listaFirmantes = doc.getFirmantes();
        if (listaFirmantes != null) {
            String nifFirmante = firmanteSanse.getUserJob().getIdentifier();
            String estadoFirma = SanseUtils.mapeoEstadoFirmante(firmanteSanse.getState().getIdentifier());
            Calendar fechaFirmante = firmanteSanse.getFstate();
            
            for (FirmaFirmanteVO firmanteFlexia : listaFirmantes) {
                // Comprobamos si el usuario cambio el estado
                if (StringUtils.equals(firmanteFlexia.getDocumento(), nifFirmante)) {
                    if (!StringUtils.equals(firmanteFlexia.getEstadoFirma(), estadoFirma)) {
                        firmanteActualizar = firmanteFlexia.copy();
                        firmanteActualizar.setEstadoFirma(estadoFirma);
                        firmanteActualizar.setFechaFirma(fechaFirmante);
                    }
                    
                    break;
                }
            }
        }

        return firmanteActualizar;
    }
    
    /**
     * Actualiza el estado de las firmas.
     * 
     * @param codOrganizacion
     * @param codUsuario
     * @param docActualizar
     * @param params 
     * @throws es.altia.flexia.portafirmasexternocliente.exception.PortafirmasExternoException 
     */
    public void actualizarEstadosFirmasFlujosUsuarios(
            Integer codOrganizacion, Integer codUsuario,
            FirmaDocumentoTramitacionVO docActualizar, String[] params)
            throws PortafirmasExternoException {
        
        log.debug("actualizarEstadosFirmasFlujosUsuarios() : BEGIN");

        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            if (docActualizar != null) {
                FirmaDocumentoTramiteClave clave = docActualizar.getClave();
                
                if (!StringUtils.equals(
                        ConstantesPortafirmas.ESTADO_FIRMA_SIN_CAMBIOS,
                        docActualizar.getEstadoFirma())) {
                    
                    if (docActualizar.getFichero() != null) {
                        log.debug("Se almacena el fichero firmado");
                        
                        GeneralValueObject paramAlmacen = new GeneralValueObject();
                        paramAlmacen.setAtributo("codMunicipio", Integer.toString(clave.getCodMunicipio()));
                        paramAlmacen.setAtributo("codAplicacion", "");
                        paramAlmacen.setAtributo("codProcedimiento", clave.getCodProcedimiento());
                        paramAlmacen.setAtributo("ejercicio", Integer.toString(clave.getEjercicio()));
                        paramAlmacen.setAtributo("numeroExpediente", clave.getNumExpediente());
                        paramAlmacen.setAtributo("codTramite", Integer.toString(clave.getCodTramite()));
                        paramAlmacen.setAtributo("ocurrenciaTramite", Integer.toString(clave.getCodOcurrencia()));
                        paramAlmacen.setAtributo("codDocumento", "");
                        paramAlmacen.setAtributo("codUsuario", Integer.toString(codUsuario));
                        paramAlmacen.setAtributo("numeroDocumento", Integer.toString(clave.getCodDocumento()));
                        paramAlmacen.setAtributo("nombreDocumento", docActualizar.getNombre());
                        paramAlmacen.setAtributo("ficheroWord", docActualizar.getFichero());
                        
                        documentoManager.grabarDocumento(paramAlmacen, params);
                    }
                    
                    log.debug("Se actualiza la firma del documento");
                    pluginCommonDao.actualizarDocumentoEstadoFirmasFlujoUsuarios(docActualizar, con);
                }
                
                log.debug("Se actualizan las firmas de los firmantes");
                pluginCommonDao.actualizarFirmantesEstadoFirmasFlujoUsuarios(docActualizar, con);
            }
            
            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            
            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException ex1) {
                log.error(ex1.getMessage());
            }
            
            throw new PortafirmasExternoException(ERROR_ACTUALIZAR_ESTADO_FIRMAS_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.debug("actualizarEstadosFirmasFlujosUsuarios() : END");
    }

    /**
     * Obtiene el asunto y el texto para enviar la peticion al portafirmas SANSE
     * con el contenido sustituido por el valor correspondiente
     * 
     * @param codOrganizacion 
     * @param datos 
     * @throws TechnicalException
     */
    private void getAsuntoYTexto(
            Integer codOrg, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTram, Integer ocuTram,
            Integer codDoc, GeneralValueObject datos, Connection conexion)
            throws TechnicalException {
        
        String codOrganizacion = Integer.toString(codOrg);
        DocumentoTramitacionVO documento = (DocumentoTramitacionVO) datos.getAtributo(
                ConstantesPortafirmasSanse.SANSE_PARAM_DOCUMENTO_TRAMITE);
        
        // Asunto
        String asunto = SanseUtils.getPropiedadSanse(
                codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_ASUNTO);
        
        asunto = asunto.replaceAll("@expediente@", numExpediente);
        
        datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_ASUNTO, asunto);
        
        // Texto
        String texto = SanseUtils.getPropiedadSanse(
                codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_TEXTO);
        String nombreTramite = tramitacionExpedientesDao.getNombreTramite(
                codOrg, codProcedimiento, codTram, conexion);
        
        texto = texto.replaceAll("@expediente@", numExpediente);
        texto = texto.replaceAll("@documento@", documento.getDescripcion());
        texto = texto.replaceAll("@tramite@", nombreTramite);

        datos.setAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TEXTO, texto);
    }

}
