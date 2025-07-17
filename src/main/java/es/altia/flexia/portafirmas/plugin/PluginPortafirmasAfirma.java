package es.altia.flexia.portafirmas.plugin;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.interfaces.OpcionesFirma;
import es.altia.merlin.licitacion.commons.utils.integraciones.interfaces.PlataformaPKI;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import java.util.Date;
import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PluginPortafirmasAfirma implements PluginPortafirmas {

    protected static Log m_Log =
            LogFactory.getLog(PluginPortafirmasAfirma.class.getName());

    public boolean verificarFirma(DocumentoFirmadoVO documento) throws TechnicalException {

        PlataformaPKI instance;
        try {
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra").newInstance();
            m_Log.debug("FactoriaPKI.getPlataformaPKI() - es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra");
            instance.init("afirma.properties");
            OpcionesFirma opcionesVerificacion = new OpcionesFirma();
            opcionesVerificacion.setFechaValidacion(new Date());
            ArrayListFirmasVO resultado = instance.verificarFirma(documento.getFirma(), 
                    Base64.decode(documento.getFicheroHash64()), null, null, opcionesVerificacion);

            if (resultado.getGlobalValidity()) {
                return true;
            } else {
                return false;
            }

        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch(Exception ex){
            throw new TechnicalException(ex.getMessage());
        }

    }
    
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException {
       
        PlataformaPKI instance;
        try {
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra").newInstance();
            m_Log.debug("FactoriaPKI.getPlataformaPKI() - es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra");
            instance.init("afirma.properties");
            OpcionesFirma opcionesVerificacion = new OpcionesFirma();
            opcionesVerificacion.setFechaValidacion(new Date());
            ArrayListFirmasVO resultado = instance.verificarFirma(documento.getFirma(), 
                    Base64.decode(documento.getFicheroHash64()), null, null, opcionesVerificacion);

           return resultado;
           
        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch(Exception ex){
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
            instance = (PlataformaPKI) Class.forName("es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra").newInstance();
            m_Log.debug("FactoriaPKI.getPlataformaPKI() - es.altia.sle.commons.pki.afirmaIntegra.PKIafirmaIntegra");
            instance.init("afirma.properties");
            firmaVO = instance.firmaServidor(documento,null);
            
            if(firmaVO!=null && firmaVO.getGlobalValidity()){
                  m_Log.debug("Se ha obtenido la firma del documento con certificado de organismo de AFIRMA");  
            }else
                m_Log.debug("No ha obtenido la firma del documento con certificado de organismo de AFIRMA");  

        } catch (InstantiationException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new TechnicalException(ex.getMessage());
        } catch (Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
        
        return firmaVO.getFirma();
    }
}
