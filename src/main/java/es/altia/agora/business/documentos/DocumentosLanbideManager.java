/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.documentos;

import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.manager.MeLanbideDokusiManager;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.ConstantesMeLanbide_Dokusi;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Clse en la cual solamente se realizan operaciones en las tablas de LANBIDE
 * @author jesus.cordoba-perez
 */
public class DocumentosLanbideManager {
    
    private static DocumentosLanbideManager instance = null;
    
    private static final Log log = LogFactory.getLog(DocumentosLanbideManager.class.getName());

    /**
     * Factory method para el <code>Singelton</code>.
     *
     * @return La unica instancia de DocumentosLanbideManager
     */
    public static DocumentosLanbideManager getInstance() {
        synchronized (DocumentosLanbideManager.class) {
            if (instance == null) {
                instance = new DocumentosLanbideManager();
            }
        }
        return instance;
    }

  
    /**
     * Metodo el cual se obtiene el OID de un documento enviandole 
     * como parametros de entrada los datos del documento 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numerDocumento
     * @param params
     * @return 
     */
    public String obtenerOIDDocumento (Integer codMunicipio, Integer ejercicio, String codProcedimiento, String numExpediente, 
            Integer codTramite, Integer ocurrenciaTramite, String numerDocumento, String[] params) throws Exception {
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        
        return this.obtenerOIDDocumento(codMunicipio, ejercicio, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, numerDocumento, adapt);
    }
    
    /**
     * Metodo el cual se obtiene el OID de un documento enviandole 
     * como parametros de entrada los datos del documento 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numerDocumento
     * @param adapt
     * @return 
     */
    public String obtenerOIDDocumento (Integer codMunicipio, Integer ejercicio, String codProcedimiento, String numExpediente, 
            Integer codTramite, Integer ocurrenciaTramite, String numerDocumento, AdaptadorSQLBD adapt) throws Exception {
        
        String OIDDocumento = "";
        
        //Se obtiene el OID del documento de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT 
        try {
            log.debug("Se realiza la consulta para obtener el OID del documento");
            
            DocumentoGestor documentoGestor = new DocumentoGestor();
            
            log.debug("municipio vale: " + codMunicipio);
            log.debug("ejercicio vale: " + ejercicio);
            log.debug("procedimiento vale: " + codProcedimiento);
            log.debug("numExpediente vale: " + numExpediente);
            log.debug("Tramite vale: " + codTramite);
            log.debug("Ocurrencia vale: " + ocurrenciaTramite);
            log.debug("Num Documento vale: " + numerDocumento);
            
            documentoGestor.setCodMunicipio(codMunicipio);
            documentoGestor.setEjercicio(ejercicio);
            documentoGestor.setCodProcedimiento(codProcedimiento);
            documentoGestor.setCodTramite(codTramite);
            documentoGestor.setNumeroExpediente(numExpediente);
            documentoGestor.setOcurrenciaTramite(ocurrenciaTramite);
            documentoGestor.setNumeroDocumento(numerDocumento);
            
            OIDDocumento  = MeLanbideDokusiManager.getInstance().recuperaOIDDocumentoConDatosFlexia(documentoGestor, 
                    ConstantesMeLanbide_Dokusi.CODIGO_DOCUMENTO_TRAMITACION, adapt);
   
        } catch (Exception ex) {
            log.error("Se ha producido un error al obtener el OID del documento " + ex.getMessage());
            ex.printStackTrace();
            throw new Exception ("Se ha producido un error al obtener el OID del documento: " + ex.getMessage());
        }//try-catch
        
        log.debug("OID Documento vale: " + OIDDocumento);
        
        return OIDDocumento;
    }
    
}
