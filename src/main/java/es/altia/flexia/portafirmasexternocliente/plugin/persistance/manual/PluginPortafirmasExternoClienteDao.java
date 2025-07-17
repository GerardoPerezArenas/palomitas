package es.altia.flexia.portafirmasexternocliente.plugin.persistance.manual;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramitacionVO;
import es.altia.agora.business.sge.firma.vo.FirmaFirmanteVO;
import es.altia.agora.business.sge.firma.vo.FirmaTipoVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.SanseUtils;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.altia.flexia.portafirmasexternocliente.vo.InfoTramiteVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.jdbc.JdbcOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class PluginPortafirmasExternoClienteDao {
    
    //Logger
    private static Logger log = Logger.getLogger(PluginPortafirmasExternoClienteDao.class);
    
    //Instance
    private static PluginPortafirmasExternoClienteDao instance = null;
    
    /**
     * Recupera una instancia de la clase
     * 
     * @return PluginPortafirmasExternoClienteDao
     */
    public static PluginPortafirmasExternoClienteDao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(PluginPortafirmasExternoClienteDao.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Creamos una nueva instancia de la clase");
                    instance = new PluginPortafirmasExternoClienteDao();
                }//if(instance == null)
            }//synchronized(PluginPortafirmasExternoClienteDao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    private final String SQL_DOCUMENTO_TRAMITACION = "Select CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_FIL, CRD_DES, CRD_NUD"
            + " From E_CRD where CRD_MUN = ? And CRD_PRO = ? And CRD_EJE = ? And CRD_NUM = ? And CRD_TRA = ? And CRD_OCU = ? And CRD_NUD = ?";
    
    public DocumentoTramitacionVO getDocumentoTramitacion(String codOrganizacion, String codProcedimiento, String numExpediente, 
            String codTramite, String ocurrenciaTramite, String codDocumento, Connection con, String[] params, String portafirmas) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : BEGIN");
        ResultSet rs = null;
        PreparedStatement ps = null;
        DocumentoTramitacionVO doc = null;
        try{
            String[] datosExp = numExpediente.split("/");
                String ejercicio = datosExp[0];
            
            ps = con.prepareStatement(SQL_DOCUMENTO_TRAMITACION);
            if(log.isDebugEnabled()) {
                log.debug("SQL = " + SQL_DOCUMENTO_TRAMITACION);
                log.debug("codOrganizacion: " + codOrganizacion);
                log.debug("codProcedimiento: " + codProcedimiento);
                log.debug("ejercicio: " + ejercicio);
                log.debug("numExpediente: " + numExpediente);
                log.debug("codTramite: " + codTramite);
                log.debug("ocurrenciaTramite: " + ocurrenciaTramite);
                log.debug("codDocumento: " + codDocumento);
            }
            ps.setString(1, codOrganizacion);
            ps.setString(2, codProcedimiento);
            ps.setString(3, ejercicio);
            ps.setString(4, numExpediente);
            ps.setString(5, codTramite);
            ps.setString(6, ocurrenciaTramite);
            ps.setString(7, codDocumento);
            
            rs = ps.executeQuery();
            while (rs.next()){
                doc = new DocumentoTramitacionVO();
                doc.setCodMunicipio(rs.getString("CRD_MUN"));
                doc.setCodProcedimiento(rs.getString("CRD_PRO"));
                doc.setEjercicio(rs.getString("CRD_EJE"));
                doc.setNumExpediente(rs.getString("CRD_NUM"));
                doc.setCodTramite(rs.getString("CRD_TRA"));
                doc.setCodOcurrencia(rs.getString("CRD_OCU"));
                //doc.setFichero(rs.getBytes("CRD_FIL"));
                doc.setDescripcion(rs.getString("CRD_DES"));
                doc.setCodDocumento(rs.getString("CRD_NUD"));
                if (portafirmas != null && "LAN".equals(portafirmas)) {
                    log.debug("El protafirmas es el utilizado para Lanbide y NO obtendra el documento");
                } else {
                    log.debug("El protafirmas es distinto al utilizado para Lanbide y obtendra el documento");
                    getDocumentoTramitacionFromAlmacen(doc, params);
                }
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el documento de tramitación = " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : END");
        return doc;
    }//getDocumentoTramitacion
    
    private void getDocumentoTramitacionFromAlmacen(DocumentoTramitacionVO documento, String[] params){
        try {
            if(log.isDebugEnabled()) log.debug("getDocumentoTramitacionFromAlmacen() : BEGIN");
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos necesarios para recuperar el documento de la factoria");
            Hashtable<String,Object> datos = new Hashtable<String, Object>();
            datos.put("codMunicipio",documento.getCodMunicipio());
            datos.put("codProcedimiento",documento.getCodProcedimiento());
            datos.put("ejercicio",documento.getEjercicio());
            datos.put("numeroExpediente",documento.getNumExpediente());
            datos.put("codTramite",documento.getCodTramite());
            datos.put("ocurrenciaTramite",documento.getCodOcurrencia());
            datos.put("codDocumento",documento.getCodDocumento());
            datos.put("perteneceRelacion","false");
            datos.put("params",params);                
            datos.put("nombreDocumento",documento.getDescripcion());
            datos.put("numeroDocumento",documento.getCodDocumento());

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(documento.getCodMunicipio()).getImplClassPluginProcedimiento(documento.getCodMunicipio(),documento.getCodProcedimiento());            
            Documento doc = null;
            int tipoDocumento = -1;
            
            if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            else{
                String codigoVisibleTramite = DefinicionTramitesManager.getInstance().
                        getCodigoVisibleTramite(documento.getCodMunicipio(),documento.getCodProcedimiento(),
                        documento.getCodTramite(), params);
                
                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().
                        getDescripcionProcedimiento(documento.getCodProcedimiento(), params);
                
                String editorTexto = "";
                datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                try{
                     editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(Integer.parseInt(documento.getCodMunicipio()),
                        documento.getNumExpediente(),Integer.parseInt(documento.getCodTramite()), 
                        Integer.parseInt(documento.getCodOcurrencia()),Integer.parseInt(documento.getCodDocumento()),false,params);
                 }catch (NumberFormatException e)
                {
                log.error("No se puede obtener el editor de texto definido debido a una excepcion");
                }

                String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                        editorTexto, documento.getDescripcion());
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                datos.put("tipoMime", tipoMime);

                datos.put("nombreOrganizacion",documento.getCodMunicipio());
                datos.put("nombreProcedimiento",nombreProcedimiento);
                datos.put("codigoVisibleTramite",codigoVisibleTramite);
                datos.put("numeroDocumento",documento.getCodDocumento());

                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");            
                String carpetaRaiz    = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + documento.getCodMunicipio() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                
                String descripcionOrganizacion = documento.getCodMunicipio();
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(documento.getCodMunicipio() + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(documento.getCodProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(documento.getNumExpediente().replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                datos.put("listaCarpetas",listaCarpetas);

                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            }//getDocumentoTramitacionFromAlmacen
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
            log.debug("doc.getNumeroExpediente(): " + doc.getNumeroExpediente());
            log.debug("doc.getCodDocumento: " + doc.getCodDocumento());
            log.debug("doc.getNombreDocumento(): " + doc.getNombreDocumento());
            log.debug("doc.getExtension(): " + doc.getExtension());
            byte[] bytes = almacen.getDocumento(doc);
            documento.setFichero(bytes);
            if(log.isDebugEnabled()) log.debug("getDocumentoTramitacionFromAlmacen() : END");
        } catch (AlmacenDocumentoTramitacionException ex) {
            log.error("Se ha producido un error recuperando el documento del almacen " + ex.getMessage());
        }//try-catch
    }//getDocumentoTramitacionFromAlmacen
    
    private final String SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION = "Select  USU_NIF "
            + " FROM E_DOT, E_DOT_FIR, E_CRD, " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU"
            + " WHERE E_DOT.DOT_MUN = E_DOT_FIR.DOT_MUN AND E_DOT.DOT_PRO = E_DOT_FIR.DOT_PRO AND E_DOT.DOT_TRA = E_DOT_FIR.DOT_TRA"
            + " AND E_DOT.DOT_COD = E_DOT_FIR.DOT_COD AND E_DOT_FIR.USU_COD = A_USU.USU_COD AND E_DOT_FIR.DOT_COD = E_CRD.CRD_DOT"
            + " AND E_DOT.DOT_MUN = ? AND E_DOT.DOT_PRO = ? AND E_DOT.DOT_TRA = ? AND E_CRD.CRD_NUD = ? AND E_CRD.CRD_NUM = ?";
    
    private final String SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION_PORTAFIRMAS = "Select  USU_NIF, USU_NOM, USU_BUZFIR "
            + " FROM E_CRD_FIR_FIRMANTES FIR, " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU"
            + " WHERE FIR.ID_USUARIO = A_USU.USU_COD"
            + " AND FIR.COD_MUNICIPIO = ? AND FIR.COD_PROCEDIMIENTO = ? AND FIR.COD_TRAMITE = ? AND FIR.COD_DOCUMENTO = ? AND FIR.NUM_EXPEDIENTE = ?";
    
    public DocumentoTramitacionVO getDatosFirmaDocumentoTramitacion(DocumentoTramitacionVO documentoTramitacion, Connection con, String portafirmas) 
            throws Exception{
        if(log.isDebugEnabled()) log.debug("getDatosFirmaDocumentoTramitacion() : BEGIN");
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                ps = con.prepareStatement(SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION_PORTAFIRMAS);
                if(log.isDebugEnabled()) log.debug("SQL = " + SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION_PORTAFIRMAS);
            } else {
                ps = con.prepareStatement(SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION);
                if(log.isDebugEnabled()) log.debug("SQL = " + SQL_INFO_FIRMA_DOCUMENTO_TRAMITACION);
            }
            if(log.isDebugEnabled()) log.debug(String.format("Parámetros de la SQL = %s-%s-%s-%s-%s", documentoTramitacion.getCodMunicipio(), documentoTramitacion.getCodProcedimiento(), 
                    documentoTramitacion.getCodTramite(), documentoTramitacion.getCodDocumento(), documentoTramitacion.getNumExpediente()));
            ps.setString(1, documentoTramitacion.getCodMunicipio());
            ps.setString(2, documentoTramitacion.getCodProcedimiento());
            ps.setString(3, documentoTramitacion.getCodTramite());
            ps.setString(4, documentoTramitacion.getCodDocumento());
            ps.setString(5, documentoTramitacion.getNumExpediente());
            
            rs = ps.executeQuery();
            while (rs.next()){
                documentoTramitacion.setNifUsuarioFirmante(rs.getString("USU_NIF"));
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                     documentoTramitacion.setBuzonFirma(rs.getString("USU_BUZFIR"));
                     documentoTramitacion.setNombreUsuarioFirmante(rs.getString("USU_NOM"));
                }
                
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la info de firma del documento de tramitación = " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDatosFirmaDocumentoTramitacion() : END");
        return documentoTramitacion;
    }//getDatosFirmaDocumentoTramitacion
    
    String SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA = "Select E_CRD.CRD_MUN, E_CRD.CRD_NUD, E_CRD.CRD_FIR_EST, E_CRD.CRD_DES, E_CRD_FIR.FIR_EST, "
            + " E_CRD_FIR.CRD_COD_PF_EXT, E_CRD_FIR.CRD_ID_SOL_PF_EXT "
            + " From E_CRD, E_CRD_FIR, E_DOT "
            + " Where E_CRD.CRD_MUN  = E_CRD_FIR.CRD_MUN "
            + " And E_CRD.CRD_PRO = E_CRD_FIR.CRD_PRO "
            + " And E_CRD.CRD_EJE = E_CRD_FIR.CRD_EJE "
            + " And E_CRD.CRD_NUM = E_CRD_FIR.CRD_NUM "
            + " And E_CRD.CRD_TRA = E_CRD_FIR.CRD_TRA "
            + " And E_CRD.CRD_OCU = E_CRD_FIR.CRD_OCU "
            + " And E_CRD.CRD_NUD = E_CRD_FIR.CRD_NUD "
            + " And E_CRD.CRD_MUN = E_DOT.DOT_MUN "
            + " And E_CRD.CRD_PRO = E_DOT.DOT_PRO "
            + " And E_CRD.CRD_TRA = E_DOT.DOT_TRA "
            + " And E_CRD.CRD_DOT = E_DOT.DOT_COD "
            + " And E_DOT.DOT_FRM = 'O' "
            + " And E_CRD.CRD_FIR_EST = 'O' "
            + " And E_CRD_FIR.FIR_EST = 'O' "
            + " And E_CRD_FIR.CRD_COD_PF_EXT = ?"
            + " And E_CRD.CRD_TRA = ? "
            + " And E_CRD.CRD_OCU = ? "
            + " And E_CRD.CRD_PRO = ? "
            + " And E_CRD.CRD_NUM = ? ";
    
    public ArrayList<DocumentoTramitacionVO> getDocumentosTramitacionFirmaPendiente(String codOrganizacion, String codProcedimiento, 
            String numExpediente, String codTramite, String ocurrenciaTramite,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentosTramitacionFirmaPendiente() : BEGIN");
        ArrayList<DocumentoTramitacionVO> listaDocumentos = new ArrayList<DocumentoTramitacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos los documentos pendientes de firma para el cliente de portafirmas seleccinado");
            String clientePortafirmas = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(codOrganizacion);
            if(log.isDebugEnabled()) log.debug("Cliente portafirmas = " + clientePortafirmas);
            ps = con.prepareStatement(SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA);
            ps.setString(1, clientePortafirmas);
            ps.setInt(2, Integer.valueOf(codTramite));
            ps.setInt(3, Integer.valueOf(ocurrenciaTramite));
            ps.setString(4, codProcedimiento);
            ps.setString(5, numExpediente);
            rs = ps.executeQuery();
            while (rs.next()){
                DocumentoTramitacionVO doc = new DocumentoTramitacionVO();
                    doc.setCodMunicipio(rs.getString("CRD_MUN"));
                    doc.setCodDocumento(String.valueOf(rs.getInt("CRD_NUD")));
                    doc.setDescripcion(rs.getString("CRD_DES"));
                    doc.setEstadoFirma(rs.getString("FIR_EST"));
                    doc.setClientePortafirmasExterno(rs.getString("CRD_COD_PF_EXT"));
                    doc.setIdSolicitudPortafirmasExterno(rs.getString("CRD_ID_SOL_PF_EXT"));
                    doc.setCodTramite(codTramite);
                    doc.setCodOcurrencia(ocurrenciaTramite);
                    doc.setCodProcedimiento(codProcedimiento);
                    doc.setNumExpediente(numExpediente);
                listaDocumentos.add(doc);
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar la lista de documentos de tramitacion pendientes " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDocumentosTramitacionFirmaPendiente() : END");
        return listaDocumentos;
    }//getDocumentosTramitacionFirmaPendiente
    
    String SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA_FLUJO_USUARIOS =
            " SELECT crd.CRD_NUD AS COD_DOCUMENTO, "
          + "   crd.CRD_DES AS NOMBRE_DOCUMENTO, "
          + "   crd.CRD_FIR_EST AS ESTADO_FIRMA_CRD, "
          + "   fir.FIR_EST AS ESTADO_FIRMA_E_CRD_FIR, "
          + "   fir.CRD_COD_PF_EXT AS PORTAFIRMAS_EXTERNO, "
          + "   fir.CRD_ID_SOL_PF_EXT AS COD_SOLICITUD_PORTAFIRMAS, "
          + "   fusu.ID_USUARIO AS ID_USUARIO, "
          + "   usu.USU_NIF AS DOCUMENTO_USUARIO, "
          + "   fusu.ESTADO_FIRMA AS ESTADO_FIRMA_USUARIO "
          + " FROM E_CRD crd "
          + " INNER JOIN E_CRD_FIR fir ON crd.CRD_MUN = fir.CRD_MUN "
          + "                         AND crd.CRD_PRO = fir.CRD_PRO "
          + "                         AND crd.CRD_EJE = fir.CRD_EJE "
          + "                         AND crd.CRD_NUM = fir.CRD_NUM "
          + "                         AND crd.CRD_TRA = fir.CRD_TRA "
          + "                         AND crd.CRD_OCU = fir.CRD_OCU "
          + "                         AND crd.CRD_NUD = fir.CRD_NUD "
          + " INNER JOIN E_CRD_FIR_FIRMANTES fusu ON fusu.COD_MUNICIPIO = fir.CRD_MUN "
          + "                         AND fusu.COD_PROCEDIMIENTO = fir.CRD_PRO "
          + "                         AND fusu.EJERCICIO = fir.CRD_EJE "
          + "                         AND fusu.NUM_EXPEDIENTE = fir.CRD_NUM "
          + "                         AND fusu.COD_TRAMITE = fir.CRD_TRA "
          + "                         AND fusu.COD_OCURRENCIA = fir.CRD_OCU "
          + "                         AND fusu.COD_DOCUMENTO = fir.CRD_NUD "
          + " INNER JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU usu ON fusu.ID_USUARIO = usu.USU_COD "
          + " WHERE crd.CRD_MUN = ? "
          + "   AND crd.CRD_PRO = ? "
          + "   AND crd.CRD_EJE = ? "
          + "   AND crd.CRD_NUM = ? "
          + "   AND crd.CRD_TRA = ? "
          + "   AND crd.CRD_OCU = ? "
          + "   AND crd.CRD_FIR_EST = 'O' "
          + "   AND fir.FIR_EST = 'O' "
          + "   AND fir.CRD_COD_PF_EXT = ? ";
    
    public ArrayList<FirmaDocumentoTramitacionVO> getDocumentosTramitacionFirmaPendienteFlujoUsuario(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer ocuTramite, Connection con)
            throws Exception {
        
        log.debug("getDocumentosTramitacionFirmaPendienteFlujoUsuario() : BEGIN");
        
        ArrayList<FirmaDocumentoTramitacionVO> listaDocumentos = new ArrayList<FirmaDocumentoTramitacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        try{
            log.debug("Recuperamos los documentos pendientes de firma de flujo/usuario para el cliente de portafirmas seleccionado");
            
            String clientePortafirmas = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(Integer.toString(codOrganizacion));
            if(log.isDebugEnabled()) {
                log.debug(String.format("Cliente portafirmas = %s", clientePortafirmas));
                log.debug(String.format("SQL = %s", SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA_FLUJO_USUARIOS));
            }
            
            ps = con.prepareStatement(SQL_RECUPERA_DOCS_TRAMITACION_PENDIENTES_DE_FIRMA_FLUJO_USUARIOS);
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(ps, indexStart,
                    codOrganizacion,
                    codProcedimiento,
                    ejercicio,
                    numExpediente,
                    codTramite,
                    ocuTramite,
                    clientePortafirmas);

            rs = ps.executeQuery();
            
            FirmaDocumentoTramitacionVO doc = null;
            FirmaDocumentoTramiteClave clave = null;
            List<FirmaFirmanteVO> listaFirmantes = null;
            FirmaFirmanteVO firmante = null;
            int numDocumentoAnterior = -1;
            int numDocumento = -1;
            while (rs.next()){
                numDocumento = rs.getInt("COD_DOCUMENTO");
                
                if (numDocumentoAnterior != numDocumento) {
                    numDocumentoAnterior = numDocumento;
                    
                    clave = new FirmaDocumentoTramiteClave();
                    clave.setCodMunicipio(codOrganizacion);
                    clave.setCodProcedimiento(codProcedimiento);
                    clave.setEjercicio(ejercicio);
                    clave.setNumExpediente(numExpediente);
                    clave.setCodTramite(codTramite);
                    clave.setCodOcurrencia(ocuTramite);
                    clave.setCodDocumento(numDocumento);
                    
                    listaFirmantes = new ArrayList<FirmaFirmanteVO>();
                    
                    doc = new FirmaDocumentoTramitacionVO();                    
                    doc.setClave(clave);
                    doc.setFirmantes(listaFirmantes);
                    doc.setNombre(rs.getString("NOMBRE_DOCUMENTO"));
                    String codSolicitudUnificado = rs.getString("COD_SOLICITUD_PORTAFIRMAS");
                    doc.setIdSolicitudPortafirmasExterno(SanseUtils.extraerIdPeticion(codSolicitudUnificado));
                    doc.setIdDocumentoPortafirmasExterno(SanseUtils.extraerIdDocumento(codSolicitudUnificado));
                    doc.setClientePortafirmasExterno(rs.getString("PORTAFIRMAS_EXTERNO"));
                    doc.setEstadoFirma(rs.getString("ESTADO_FIRMA_CRD"));
                    
                    listaDocumentos.add(doc);
                }
                
                firmante = new FirmaFirmanteVO();
                firmante.setId(rs.getInt("ID_USUARIO"));
                firmante.setDocumento(rs.getString("DOCUMENTO_USUARIO"));
                firmante.setEstadoFirma(rs.getString("ESTADO_FIRMA_USUARIO"));
                listaFirmantes.add(firmante);
            }
        }catch(Exception ex){
            log.error(String.format(
                    "Se ha producido un error al recuperar la lista de documentos de tramitacion pendientes: %s",
                    ex.getMessage()));
            throw ex;
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        
        log.debug("getDocumentosTramitacionFirmaPendienteFlujoUsuario() : END");
        
        return listaDocumentos;
    }
    
    String SQL_ACTUALIZA_ESTADO_E_CRD = "Update E_CRD set CRD_FIR_EST = ? "
            + " where E_CRD.CRD_PRO = ? "
            + " and E_CRD.CRD_NUM = ? "
            + " and E_CRD.CRD_TRA = ? "
            + " and E_CRD.CRD_OCU = ? "
            + " and E_CRD.CRD_NUD = ? "
            + " and E_CRD.CRD_MUN = ? ";
    
    String SQL_ACTUALIZA_ESTADO_E_CRD_FIR = "Update E_CRD_FIR set FIR_EST = ?, USU_FIR = ? "
            + " where E_CRD_FIR.CRD_PRO = ? "
            + " and E_CRD_FIR.CRD_NUM = ? "
            + " and E_CRD_FIR.CRD_TRA = ? "
            + " and E_CRD_FIR.CRD_OCU = ? "
            + " and E_CRD_FIR.CRD_NUD = ? "
            + " and E_CRD_FIR.CRD_MUN = ? ";
    
    public void actualizaEstadoDocumento(DocumentoTramitacionVO doc, String estado, Integer codUsuarioFirmante, Connection con) 
            throws Exception{
        if(log.isDebugEnabled()) log.debug("actualizaEstadoDocumento() : BEGIN");
        PreparedStatement ps = null;
        try{
            if(log.isDebugEnabled()) log.debug("Actualizamos el estado de la tabla E_CRD");
            ps = con.prepareStatement(SQL_ACTUALIZA_ESTADO_E_CRD);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_ACTUALIZA_ESTADO_E_CRD);
            //Parametros
            ps.setString(1, estado);
            //Where
            ps.setString(2, doc.getCodProcedimiento());
            ps.setString(3, doc.getNumExpediente());
            ps.setInt(4, Integer.valueOf(doc.getCodTramite()));
            ps.setInt(5, Integer.valueOf(doc.getCodOcurrencia()));
            ps.setInt(6, Integer.valueOf(doc.getCodDocumento()));
            ps.setInt(7, Integer.valueOf(doc.getCodMunicipio()));
            
            int filasActualizadas = ps.executeUpdate();
            if(filasActualizadas > 0){
                if(log.isDebugEnabled()) log.debug("Actualizamos el estado de la tabla E_CRD_FIR");
                ps = con.prepareStatement(SQL_ACTUALIZA_ESTADO_E_CRD_FIR);
                //Parametros
                ps.setString(1, estado);
                ps.setInt(2, codUsuarioFirmante);
                //Where
                ps.setString(3, doc.getCodProcedimiento());
                ps.setString(4, doc.getNumExpediente());
                ps.setInt(5, Integer.valueOf(doc.getCodTramite()));
                ps.setInt(6, Integer.valueOf(doc.getCodOcurrencia()));
                ps.setInt(7, Integer.valueOf(doc.getCodDocumento()));
                ps.setInt(8, Integer.valueOf(doc.getCodMunicipio()));
                ps.executeUpdate();
            }//if(filasActualizadas > 0)
        }catch(Exception ex){
            log.error("Se ha producido un error actualizando el estado de firma del documento " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizaEstadoDocumento() : END");
    }//public void actualizaEstadoDocumento(DocumentoTramitacionVO doc, String estado)
    
    
    String SQL_BUSCA_USUARIO = "Select USU_COD from " + GlobalNames.ESQUEMA_GENERICO + "A_USU where USU_NIF = ?";
    
    public Integer getCodUsuarioPorNif (String nif, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodUsuarioPorNif() : BEGIN");
        ResultSet rs = null;
        PreparedStatement ps = null;
        Integer doc = null;
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos el usuario firmante por su documento");
            ps = con.prepareStatement(SQL_BUSCA_USUARIO);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_BUSCA_USUARIO);
            ps.setString(1, nif);
            
            rs = ps.executeQuery();
            while (rs.next()){
                doc = rs.getInt("USU_COD");
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el numero de usuario por documento del usuario firmante");
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCodUsuarioPorNif() : END");
        return doc;
    }//getCodUsuarioPorNif
    
    String SQL_INFO_TRAMITE = " Select E_TML.TML_VALOR, E_CRO.CRO_FEI "
            + " From E_CRO, E_TML "
            + " Where E_CRO.CRO_PRO = E_TML.TML_PRO "
            + " And E_CRO.CRO_NUM = ? "
            + " And E_CRO.CRO_TRA = ? "
            + " And E_CRO.CRO_OCU = ? ";
    
    public InfoTramiteVO getInfoTramite (String codOrganizacion, String numExpediente, String codTramite, 
            String ocurrenciaTramite,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getInfoTramite() : BEGIN");
        InfoTramiteVO infoTramite = new InfoTramiteVO();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(SQL_INFO_TRAMITE);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_INFO_TRAMITE);
            ps.setString(1, numExpediente);
            ps.setInt(2, Integer.valueOf(codTramite));
            ps.setInt(3, Integer.valueOf(ocurrenciaTramite));
            rs = ps.executeQuery();
            while (rs.next()){
                infoTramite.setDescripcionTramite(rs.getString("TML_VALOR"));
                java.sql.Date fecha = rs.getDate("CRO_FEI");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                infoTramite.setFechaInicioTramite(sdf.format(fecha));
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la informacion del tramite " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getInfoTramite() : END");
        return infoTramite;
    }//getInfoTramite
    
    String SQL_TRAMITES_PENDIENTES = "Select CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_MUN, CRO_OCU "
            + " From E_CRO "
            + " Where CRO_NUM = ? "
            + " And CRO_FEF is null "
            + " UNION ALL Select CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_MUN, CRO_OCU "
            + " FROM HIST_E_CRO"
            + " WHERE CRO_NUM = ?  And CRO_FEF is null";
    
    public ArrayList<TramitacionExpedientesValueObject> getTramitesPendientes(String codOrganizacion, String numExpediente, Connection con)
            throws Exception{
        ArrayList<TramitacionExpedientesValueObject> tramitesPendientes = new ArrayList<TramitacionExpedientesValueObject>();
        if(log.isDebugEnabled()) log.debug("getTramitesPendientes() : BEGIN");
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(SQL_TRAMITES_PENDIENTES);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_TRAMITES_PENDIENTES);
            ps.setString(1, numExpediente);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();
            while (rs.next()){
                TramitacionExpedientesValueObject tramite = new TramitacionExpedientesValueObject();
                    tramite.setCodMunicipio(rs.getString("CRO_MUN"));
                    tramite.setNumeroExpediente(rs.getString("CRO_NUM"));
                    tramite.setEjercicio(rs.getString("CRO_EJE"));
                    tramite.setCodProcedimiento(rs.getString("CRO_PRO"));
                    tramite.setCodTramite(rs.getString("CRO_TRA"));
                    tramite.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                    tramitesPendientes.add(tramite);
            }//while (rs.next())
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la lista de tramites pendientes para el expediente = " + numExpediente  + " " 
                    + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getTramitesPendientes() : END");
        return tramitesPendientes;
    }//getTramitesPendientes
    
    /**
     * Obtiene el listado de firmantes documento que se va a enviar al servicio
     * web.
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param con
     * @return
     * @throws TechnicalException
     */
    public List<FirmaCircuitoVO> getListadoFirmantesTramiteFlujoUsuarios(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer ocurrenciaTramite,
            Integer codDocumento, Connection con)
            throws TechnicalException {

        log.debug("getListadoFirmantesTramite");

        List<FirmaCircuitoVO> resultado = new ArrayList<FirmaCircuitoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT usu.USU_COD as ID_USUARIO, ")
                    .append("   usu.USU_NOM AS NOMBRE_USUARIO, ")
                    .append("   usu.USU_NIF AS DOCUMENTO_USUARIO, ")
                    .append("   firm.orden AS ORDEN ")
                    .append("FROM E_CRD crd ")
                    .append("   INNER JOIN E_CRD_FIR_FIRMANTES firm ON crd.CRD_MUN = firm.COD_MUNICIPIO ")
                    .append("       AND crd.CRD_PRO = firm.COD_PROCEDIMIENTO ")
                    .append("       AND crd.CRD_EJE = firm.EJERCICIO ")
                    .append("       AND crd.CRD_NUM = firm.NUM_EXPEDIENTE ")
                    .append("       AND crd.CRD_TRA = firm.COD_TRAMITE ")
                    .append("       AND crd.CRD_OCU = firm.COD_OCURRENCIA ")
                    .append("       AND crd.CRD_NUD = firm.COD_DOCUMENTO ")
                    .append("   INNER JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ON firm.ID_USUARIO = usu.USU_COD ")
                    .append("WHERE crd.CRD_MUN = ? ")
                    .append("    AND crd.CRD_PRO = ? ")
                    .append("    AND crd.CRD_EJE = ? ")
                    .append("    AND crd.CRD_NUM = ? ")
                    .append("    AND crd.CRD_TRA = ? ")
                    .append("    AND crd.CRD_OCU = ? ")
                    .append("    AND crd.CRD_NUD = ? ")
                    .append(" ORDER BY ORDEN ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener la lista de firmantes para el portafirmas: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("CRD_MUN = %d", codOrganizacion));
                log.debug(String.format("CRD_PRO = %s", codProcedimiento));
                log.debug(String.format("CRD_EJE = %d", ejercicio));
                log.debug(String.format("CRD_NUM = %s", numExpediente));
                log.debug(String.format("CRD_TRA = %d", codTramite));
                log.debug(String.format("CRD_OCU = %d", ocurrenciaTramite));
                log.debug(String.format("CRD_NUD = %d", codDocumento));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    codOrganizacion,
                    codProcedimiento,
                    ejercicio,
                    numExpediente,
                    codTramite,
                    ocurrenciaTramite,
                    codDocumento);

            rs = ps.executeQuery();

            // Leer resultados
            FirmaCircuitoVO usuario = null;
            while (rs.next()) {
                usuario = new FirmaCircuitoVO();
                usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                usuario.setDocumentoUsuario(rs.getString("DOCUMENTO_USUARIO"));
                usuario.setOrden(rs.getInt("ORDEN"));

                resultado.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }
    
    /**
     * Obtiene el tipo de firma para el documento de tramite que se va a enviar
     * al portafirmas de SANSE
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param conexion
     * @return
     * @throws TechnicalException
     */
    public FirmaTipoVO getTipoFirmaTramite(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer ocurrenciaTramite,
            Integer codDocumento, Connection conexion)
            throws TechnicalException {

        log.debug("getTipoFirmaTramite");

        FirmaTipoVO resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT tipo.ID as ID, ")
                    .append("   tipo.NOMBRE AS NOMBRE ")
                    .append("FROM E_CRD crd ")
                    .append("   INNER JOIN E_CRD_FIR_FLUJO fluj ON crd.CRD_MUN = fluj.COD_MUNICIPIO ")
                    .append("       AND crd.CRD_PRO = fluj.COD_PROCEDIMIENTO ")
                    .append("       AND crd.CRD_EJE = fluj.EJERCICIO ")
                    .append("       AND crd.CRD_NUM = fluj.NUM_EXPEDIENTE ")
                    .append("       AND crd.CRD_TRA = fluj.COD_TRAMITE ")
                    .append("       AND crd.CRD_OCU = fluj.COD_OCURRENCIA ")
                    .append("       AND crd.CRD_NUD = fluj.COD_DOCUMENTO ")
                    .append("   INNER JOIN FIRMA_TIPO tipo ON fluj.ID_TIPO_FIRMA = tipo.ID ")
                    .append("WHERE crd.CRD_MUN = ? ")
                    .append("    AND crd.CRD_PRO = ? ")
                    .append("    AND crd.CRD_EJE = ? ")
                    .append("    AND crd.CRD_NUM = ? ")
                    .append("    AND crd.CRD_TRA = ? ")
                    .append("    AND crd.CRD_OCU = ? ")
                    .append("    AND crd.CRD_NUD = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener el tipo de firma del documento de tramitacion: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("CRD_MUN = %d", codOrganizacion));
                log.debug(String.format("CRD_PRO = %s", codProcedimiento));
                log.debug(String.format("CRD_EJE = %d", ejercicio));
                log.debug(String.format("CRD_NUM = %s", numExpediente));
                log.debug(String.format("CRD_TRA = %d", codTramite));
                log.debug(String.format("CRD_OCU = %d", ocurrenciaTramite));
                log.debug(String.format("CRD_NUD = %d", codDocumento));
            }

            ps = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    codOrganizacion,
                    codProcedimiento,
                    ejercicio,
                    numExpediente,
                    codTramite,
                    ocurrenciaTramite,
                    codDocumento);

            rs = ps.executeQuery();

            // Leer resultados
            if (rs.next()) {
                resultado = new FirmaTipoVO();
                resultado.setId(rs.getInt("ID"));
                resultado.setNombre(rs.getString("NOMBRE"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Actualiza la firma de los firmantes del documento (E_CRD_FIR_FIRMANTES)
     * 
     * @param docActualizar
     * @param conexion
     * @throws TechnicalException 
     */
    public void actualizarFirmantesEstadoFirmasFlujoUsuarios(
            FirmaDocumentoTramitacionVO docActualizar, Connection conexion)
            throws TechnicalException {
        
        log.debug("actualizarFirmantesEstadoFirmasFlujoUsuarios");

        PreparedStatement ps = null;

        try {
            if (docActualizar != null && docActualizar.getFirmantes() != null && !docActualizar.getFirmantes().isEmpty()) {
                List<FirmaFirmanteVO> listaFirmantes = docActualizar.getFirmantes();
                FirmaDocumentoTramiteClave clave = docActualizar.getClave();
                
                StringBuilder sql = new StringBuilder();

                sql.append(" UPDATE E_CRD_FIR_FIRMANTES ")
                   .append(" SET ESTADO_FIRMA = ?, FECHA_FIRMA = ?")
                   .append(" WHERE COD_MUNICIPIO = ? ")
                   .append("     AND COD_PROCEDIMIENTO = ? ")
                   .append("     AND EJERCICIO = ? ")
                   .append("     AND NUM_EXPEDIENTE = ? ")
                   .append("     AND COD_TRAMITE = ? ")
                   .append("     AND COD_OCURRENCIA = ? ")
                   .append("     AND COD_DOCUMENTO = ? ")
                   .append("     AND ID_USUARIO = ? ");

                if (log.isDebugEnabled()) {
                    log.debug(String.format("SQL actualizar estado de los firmantes del documento de tramitacion: %s", sql.toString()));
                    log.debug("PARAMS:");
                    log.debug(String.format("COD_MUNICIPIO = %d", clave.getCodMunicipio()));
                    log.debug(String.format("COD_PROCEDIMIENTO = %s", clave.getCodProcedimiento()));
                    log.debug(String.format("EJERCICIO = %d", clave.getEjercicio()));
                    log.debug(String.format("NUM_EXPEDIENTE = %s", clave.getNumExpediente()));
                    log.debug(String.format("COD_TRAMITE = %d", clave.getCodTramite()));
                    log.debug(String.format("COD_OCURRENCIA = %d", clave.getCodOcurrencia()));
                    log.debug(String.format("COD_DOCUMENTO = %d", clave.getCodDocumento()));
                }
                
                for (FirmaFirmanteVO firmante : listaFirmantes) {
                    if (log.isDebugEnabled()) {
                        log.debug("PARAMS firmante:");
                        log.debug(String.format("ESTADO_FIRMA = %s", firmante.getEstadoFirma()));
                        log.debug(String.format("FECHA_FIRMA = %s", firmante.getFechaFirma()));
                        log.debug(String.format("ID_USUARIO = %d", firmante.getId()));
                    }
                    
                    ps = conexion.prepareStatement(sql.toString());

                    int indexStart = 1;
                    JdbcOperations.setValues(ps, indexStart,
                            firmante.getEstadoFirma(),
                            DateOperations.toTimestamp(firmante.getFechaFirma()),
                            clave.getCodMunicipio(),
                            clave.getCodProcedimiento(),
                            clave.getEjercicio(),
                            clave.getNumExpediente(),
                            clave.getCodTramite(),
                            clave.getCodOcurrencia(),
                            clave.getCodDocumento(),
                            firmante.getId());

                    int rows = ps.executeUpdate();
                    
                    if (rows == 1) {
                        SigpGeneralOperations.closeStatement(ps);
                    } else {
                        throw new TechnicalException(
                                "No se ha actualizado el numero de registros correctos");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Actualiza la firma del documento de tramitacion (E_CRD_FIR y E_CRD)
     * 
     * @param docActualizar
     * @param conexion
     * @throws TechnicalException 
     */
    public void actualizarDocumentoEstadoFirmasFlujoUsuarios(
            FirmaDocumentoTramitacionVO docActualizar, Connection conexion)
            throws TechnicalException {
        
        log.debug("actualizarDocumentoEstadoFirmasFlujoUsuarios");

        PreparedStatement ps = null;

        try {
            if (docActualizar != null && docActualizar.getFirmantes() != null) {
                FirmaDocumentoTramiteClave clave = docActualizar.getClave();
                
                StringBuilder sql = new StringBuilder();

                sql.append(" UPDATE E_CRD_FIR ")
                   .append(" SET ");
                
                // Firma del documento (solo firma)
                if (docActualizar.getFirma() != null) {
                    sql.append(" FIR = ?, ");
                }

                // Para firma de tipo flujo o cualquier usuario (L y U, respectivamente)
                // El USU_FIR va a null siempre, ya que los firmantes estan en la tabla E_CRD_FIRMANTES
                sql.append(" FIR_EST = ?, FX_FIRMA = ?, USU_FIR = null ")
                   .append(" WHERE CRD_MUN = ? ")
                   .append("     AND CRD_PRO = ? ")
                   .append("     AND CRD_EJE = ? ")
                   .append("     AND CRD_NUM = ? ")
                   .append("     AND CRD_TRA = ? ")
                   .append("     AND CRD_OCU = ? ")
                   .append("     AND CRD_NUD = ? ");
                
                if (log.isDebugEnabled()) {
                    log.debug(String.format("SQL actualizar estado de la firma del documento de tramitacion (E_CRD_FIR): %s", sql.toString()));
                    log.debug("PARAMS:");
                    log.debug(String.format("FIR = %s", docActualizar.getFirma()));
                    log.debug(String.format("FIR_EST = %s", docActualizar.getEstadoFirma()));
                    log.debug(String.format("FX_FIRMA = %s", docActualizar.getFechaFirma()));
                    log.debug(String.format("CRD_MUN = %d", clave.getCodMunicipio()));
                    log.debug(String.format("CRD_PRO = %s", clave.getCodProcedimiento()));
                    log.debug(String.format("CRD_EJE = %d", clave.getEjercicio()));
                    log.debug(String.format("CRD_NUM = %s", clave.getNumExpediente()));
                    log.debug(String.format("CRD_TRA = %d", clave.getCodTramite()));
                    log.debug(String.format("CRD_OCU = %d", clave.getCodOcurrencia()));
                    log.debug(String.format("CRD_NUD = %d", clave.getCodDocumento()));
                }
                    
                ps = conexion.prepareStatement(sql.toString());

                int indexStart = 1;
                
                // Firma del documento (solo firma)
                if (docActualizar.getFirma() != null) {
                    indexStart = JdbcOperations.setValues(ps, indexStart, docActualizar.getFirma());
                }
                
                indexStart = JdbcOperations.setValues(ps, indexStart,
                        docActualizar.getEstadoFirma(),
                        DateOperations.toTimestamp(docActualizar.getFechaFirma()),
                        clave.getCodMunicipio(),
                        clave.getCodProcedimiento(),
                        clave.getEjercicio(),
                        clave.getNumExpediente(),
                        clave.getCodTramite(),
                        clave.getCodOcurrencia(),
                        clave.getCodDocumento());

                int rows = ps.executeUpdate();

                if (rows == 1) {
                    SigpGeneralOperations.closeStatement(ps);

                    sql = new StringBuilder();
                    sql.append(" UPDATE E_CRD ")
                       .append(" SET CRD_FIR_EST = ? ")
                       .append(" WHERE   CRD_MUN = ? ")
                       .append("     AND CRD_PRO = ? ")
                       .append("     AND CRD_EJE = ? ")
                       .append("     AND CRD_NUM = ? ")
                       .append("     AND CRD_TRA = ? ")
                       .append("     AND CRD_OCU = ? ")
                       .append("     AND CRD_NUD = ? ");

                    if (log.isDebugEnabled()) {
                        log.debug(String.format("SQL actualizar estado de la firma del documento de tramitacion (E_CRD): %s", sql.toString()));
                        log.debug("PARAMS:");
                        log.debug(String.format("CRD_FIR_EST = %s", docActualizar.getEstadoFirma()));
                        log.debug(String.format("COD_MUNICIPIO = %d", clave.getCodMunicipio()));
                        log.debug(String.format("COD_PROCEDIMIENTO = %s", clave.getCodProcedimiento()));
                        log.debug(String.format("EJERCICIO = %d", clave.getEjercicio()));
                        log.debug(String.format("NUM_EXPEDIENTE = %s", clave.getNumExpediente()));
                        log.debug(String.format("COD_TRAMITE = %d", clave.getCodTramite()));
                        log.debug(String.format("COD_OCURRENCIA = %d", clave.getCodOcurrencia()));
                        log.debug(String.format("COD_DOCUMENTO = %d", clave.getCodDocumento()));
                    }

                    ps = conexion.prepareStatement(sql.toString());

                    indexStart = 1;
                    indexStart = JdbcOperations.setValues(ps, indexStart,
                            docActualizar.getEstadoFirma(),
                            clave.getCodMunicipio(),
                            clave.getCodProcedimiento(),
                            clave.getEjercicio(),
                            clave.getNumExpediente(),
                            clave.getCodTramite(),
                            clave.getCodOcurrencia(),
                            clave.getCodDocumento());

                    rows = ps.executeUpdate();

                    if (rows != 1) {
                        throw new TechnicalException(
                                "No se ha actualizado el numero de registros correctos (E_CRD)");
                    }
                } else {
                    throw new TechnicalException(
                            "No se ha actualizado el numero de registros correctos (E_CRD_FIR)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }
}//class
