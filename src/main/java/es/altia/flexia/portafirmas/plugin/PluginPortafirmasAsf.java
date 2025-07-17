package es.altia.flexia.portafirmas.plugin;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.interfaces.OpcionesFirma;
import es.altia.merlin.licitacion.commons.utils.integraciones.interfaces.PlataformaPKI;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.exceptions.SleException;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.FileUtils;

public class PluginPortafirmasAsf implements PluginPortafirmas{

      protected static Log m_Log =
            LogFactory.getLog(PluginPortafirmasAfirma.class.getName());
    
      private static String CONFIG_ASF="asf.properties";
      
    public boolean verificarFirma(DocumentoFirmadoVO documento) throws TechnicalException {

        PlataformaPKI instance;
        try {
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): INICIO");}
            
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.asf.PKIasf").newInstance();
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada");}
           
            instance.init(CONFIG_ASF);
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada con properties " + CONFIG_ASF);}
            
            try {
                 
                byte[] doc1 = FileUtils.readFileToByteArray(documento.getFicheroFirma());
                OpcionesFirma opcionesVerificacion = new OpcionesFirma();
                opcionesVerificacion.setFechaValidacion(new Date());

                ArrayListFirmasVO resultado = instance.verificarFirma(documento.getFirma(), 
                    Base64.decode(new String(doc1)), null, null, opcionesVerificacion);
    
                if (!resultado.isValid()){
                    resultado = instance.verificarFirma(documento.getFirma(), doc1, null, null, opcionesVerificacion);
                }
             if (resultado.isValid()) {
                m_Log.error("PluginPortafirmasAsf.verificarFirma(): Firma válida");
                return true;
            } else {
                m_Log.error("PluginPortafirmasAsf.verificarFirma(): ERROR VERIFICANDO LA FIRMA -> LA FIRMA NO ES VALIDA");
                return false;
            }
                
            } catch (IOException ex) {
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
                throw new TechnicalException(ex.getMessage());
            }catch( Exception ex){
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
                throw new TechnicalException(ex.getMessage());
            }
            
            
           

        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        }

    }
    
    
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException {

        PlataformaPKI instance;
        try {
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): INICIO");}
            
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.asf.PKIasf").newInstance();
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada");}
           
            instance.init(CONFIG_ASF);
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada con properties " + CONFIG_ASF);}
            
            try {
                 
                byte[] doc1 = FileUtils.readFileToByteArray(documento.getFicheroFirma());
                OpcionesFirma opcionesVerificacion = new OpcionesFirma();
                opcionesVerificacion.setFechaValidacion(new Date());
                ArrayListFirmasVO resultado = instance.verificarFirma(documento.getFirma(), 
                    Base64.decode(new String(doc1)), null, null, opcionesVerificacion);
    
                if (!resultado.isValid()){
                    resultado = instance.verificarFirma(documento.getFirma(), doc1, null, null, opcionesVerificacion);
                }
                return resultado;                
            } catch (IOException ex) {
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
                throw new TechnicalException(ex.getMessage());
            }  catch(Exception ex){
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
               throw new TechnicalException(ex.getMessage());
            }
            
            
           

        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        }

    }
    
    
    
    /**
     * Realiza la firma de servidor con un certificado de organismo de ASF
     * @param documento: Contenido del documento 
     * @return String con la firma
     * @throws TechnicalException 
     */
    public String firmaServidor(byte[] documento) throws TechnicalException {

        ArrayListFirmasVO firmaVO = null;
        PlataformaPKI instance;
        try {
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.firmaServidor(): INICIO");}
            
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.asf.PKIasf").newInstance();
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada");}
           
            instance.init(CONFIG_ASF);
            if (m_Log.isDebugEnabled()){ m_Log.debug("PluginPortafirmasAsf.verificarFirma(): Instancia de la clase PKIasf incializada con properties " + CONFIG_ASF);}
            
            try {
                 
                firmaVO = instance.firmaServidor(documento,null);
                
             if (firmaVO!=null && firmaVO.getGlobalValidity()){
                m_Log.error("PluginPortafirmasAsf.firmaServidor(): Se ha obtenido la firma con certificado de organismo: ");
                
            } else {
                m_Log.error("PluginPortafirmasAsf.firmaServidor(): No se ha obtenido la firma del documento con certificado de organismo");
            }
                
            } catch (SleException ex) {
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
                throw new TechnicalException(ex.getMessage());
            }catch( Exception ex){
                Logger.getLogger(PluginPortafirmasAsf.class.getName()).log(Level.SEVERE, null, ex);
                throw new TechnicalException(ex.getMessage());
            }
            

        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        }
        
        return firmaVO.getFirma();
    }
    
    
}
