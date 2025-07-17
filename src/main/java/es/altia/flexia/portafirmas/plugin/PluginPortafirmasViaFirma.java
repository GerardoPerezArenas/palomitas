package es.altia.flexia.portafirmas.plugin;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.interfaces.PlataformaPKI;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author david.caamano
 * @version 28/11/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 28-11-2012 * #97301 Edición inicial</li>
 * </ol> 
 */
public class PluginPortafirmasViaFirma implements PluginPortafirmas {
    
    protected static Log log = LogFactory.getLog(PluginPortafirmasViaFirma.class);

    /**
     * Operacion que realiza la verificacion simple (solo devuelve si la firma es correcta o no) de la firma de un fichero o hash
     * 
     * @param documento
     * @return boolean
     * @throws TechnicalException 
     */
    public boolean verificarFirma(DocumentoFirmadoVO documento) throws TechnicalException {
        if(log.isDebugEnabled()) log.debug("verificarFirma() : BEGIN");
        PlataformaPKI instance;
        try{
            instance = (PlataformaPKI) Class.forName("es.altia.flexia.viafirma.PKIviaFirma").newInstance();
            if(log.isDebugEnabled()) log.debug("es.altia.flexia.viafirma.PKIviaFirma");
            if(log.isDebugEnabled()) log.debug("instanciamos la PKI de viaFirma");
            instance.init("viaFirma.properties");
            if(log.isDebugEnabled()) log.debug("llamamos al metodo de verificarFirma");
            ArrayListFirmasVO resultado = null;/*instance.verificarFirma(documento.getFirma(), 
                Base64.decode(documento.getFicheroHash64()));
            */
            if(resultado.getGlobalValidity()){
                if(log.isDebugEnabled()) log.debug("Firma correcta");
                if(log.isDebugEnabled()) log.debug("verificarFirma() : END");
                return true;
            }else{
                if(log.isDebugEnabled()) log.debug("Firma incorrecta");
                if(log.isDebugEnabled()) log.debug("verificarFirma() : END");
                return false;
            }//if(resultado.getGlobalValidity())
        } catch (InstantiationException ex) {
            log.error("Se ha producido una excepción al instanciar la pki de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch(Exception ex){
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        }//try-catch
    }//verificarFirma

    /**
     * Metodo que realiza la verificación de la firma de un fichero o hash
     * Devuelve un objeto ArrayListFirmasVO que contiene objetos FirmaVO con los datos extraidos de la parte publica del certificado
     * firmante (documento, nombre, tipo documento...)
     * 
     * @param documento
     * @return ArrayListFirmasVO
     * @throws TechnicalException 
     */
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException {
        if(log.isDebugEnabled()) log.debug("verificarFirmaInfo() : BEGIN");
        PlataformaPKI instance;
        try{
            instance = (PlataformaPKI) Class.forName("es.altia.flexia.viafirma.PKIviaFirma").newInstance();
            if(log.isDebugEnabled()) log.debug("es.altia.flexia.viafirma.PKIviaFirma");
            if(log.isDebugEnabled()) log.debug("instanciamos la PKI de viaFirma");
            instance.init("viaFirma.properties");
            if(log.isDebugEnabled()) log.debug("llamamos al metodo de verificarFirma");
            ArrayListFirmasVO resultado = null;/*instance.verificarFirma(documento.getFirma(), 
                Base64.decode(documento.getFicheroHash64()));
            */
            if(resultado.getGlobalValidity()){
                if(log.isDebugEnabled()) log.debug("Firma correcta");
            }else{
                if(log.isDebugEnabled()) log.debug("Firma incorrecta");
            }//if(resultado.getGlobalValidity())
            if(log.isDebugEnabled()) log.debug("verificarFirma() : END");
            return resultado;
        } catch (InstantiationException ex) {
            log.error("Se ha producido una excepción al instanciar la pki de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch(Exception ex){
            log.error("Se ha producido una excepcion al verificar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        }//try-catch
    }//verificarFirmaInfo

    /**
     * Metodo que realiza la firma de servidor de un array de bytes.
     * 
     * @param documento
     * @return String
     * @throws TechnicalException 
     */
    public String firmaServidor(byte[] documento) throws TechnicalException {
        if(log.isDebugEnabled()) log.debug("firmaServidor() : BEGIN");
         String firma = null;
        PlataformaPKI instance;
        try {
            instance = (PlataformaPKI) Class.forName("es.altia.flexia.viafirma.PKIviaFirma").newInstance();
            if(log.isDebugEnabled()) log.debug("es.altia.flexia.viafirma.PKIviaFirma");
            if(log.isDebugEnabled()) log.debug("instanciamos la PKI de viaFirma");
            instance.init("viaFirma.properties");
            if(log.isDebugEnabled()) log.debug("firmamos el documento");
            firma = null;//instance.firmaServidor(documento);
            
            if(firma!=null && !"".equals(firma)){
                log.debug("Se ha obtenido la firma del documento con certificado de organismo de AFIRMA");  
            }else{
                log.debug("No ha obtenido la firma del documento con certificado de organismo de AFIRMA");  
            }//if(firma!=null && !"".equals(firma))
        } catch (InstantiationException ex) {
            log.error("Se ha producido una excepción al instanciar la pki de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("Se ha producido una excepcion al realizar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("Se ha producido una excepcion al realizar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion al realizar la firma de servidor de viaFirma " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("firmaServidor() : END");
        return firma;
    }//firmaServidor
    
}//class
