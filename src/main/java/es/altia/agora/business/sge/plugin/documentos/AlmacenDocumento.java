package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Interfaz de los plugin de almacenamiento de documentos
 * 
 * @author oscar.rodriguez
 */
public interface AlmacenDocumento{

    // Devuelve el nombre del servicio
    public String getNombreServicio();    
    // Alta de un documento de tramitación
    public boolean setDocumento(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Se recupera el contenido de un documento de tramitación pero el plugin recupera la conexión a BBDD que necesite
    public byte[] getDocumento(Documento doc) throws AlmacenDocumentoTramitacionException;    
    // Se recupera el contenido de un documento de tramitación pero se le pasa la conexión de BBDD
    public byte[] getDocumento(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;    
    // Se elimina un documento de tramitación
    public boolean eliminarDocumento(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Se guarda la firma de un documento de tramitación
    public boolean setFirmaDocumento(Documento doc) throws AlmacenDocumentoTramitacionException;
    // True si el plugin es de Bd y false en caso contrario
    public boolean isPluginGestor();
    // Alta de un documento externo
    public boolean setDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Modificar el fichero de un documento
    public boolean setDocumentoExternoFicheroCSV(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Recupera el contenido de un documento externo
    public Documento getDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Elimina un documento externo
    public boolean eliminarDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Alta de un documento de expediente que ha sido presentado
    public boolean setDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Recupera el contenido de un documento de expediente que ha sido presentado
    public Documento getDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Elimina un documento de expediente que ha sido presentado
    public boolean eliminarDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Recupera el contenido de un documento ajeno al sistema enviado al portafirmas para que sea firmado
    public DocumentoOtroFirmaVO getDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException;
    // Da de alta un documento ajeno al sistema para que pueda ser firmado en el portafirmas
    public boolean setFirmaDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException;
    // Da de alta un documento ajeno al sistema para que pueda ser firmado en el portafirmas
        
    public boolean setFirmaDocumentoExpediente(Documento doc) throws AlmacenDocumentoTramitacionException;    
    
    // Da de alta la firma de un documento externo asociado a una notificación electrónica        
    public boolean setFirmaDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException;
    
    // Recupera un documento externo adjunto a una notificación
    //public Documento getDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException;
    public Documento getDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;
    
    // Graba un archivo en un campo suplementario de tipo fichero definido a nivel de expediente
    public boolean setDocumentoDatoSuplementarioExpediente(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;
            
    // Graba un archivo en un campo suplementario de tipo fichero definido a nivel de trámite
    public boolean setDocumentoDatoSuplementarioTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;
    
    
    
    // Da de alta varios documentos asociados a una anotación de registro 
    public void setDocumentosRegistro(ArrayList<Documento> documentos,Connection con) throws AlmacenDocumentoTramitacionException;    
    // Obtiene el contenido binario de un determinado documento asociado a una anotación de registro
    // Recupera un documento de los datos suplementarios de tipo fichero definido a nivel de expediente
    public Documento getDocumentoDatosSuplementarios(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Recupera un documento de los datos suplementarios del tipo fichero definido a nivel de tramite
    public Documento getDocumentoDatosSuplementariosTramite(Documento doc) throws AlmacenDocumentoTramitacionException;    
    // Elimina un documento de los datos suplementarios del tipo fichero definido a nivel de expediente
    public boolean eliminarDocumentoDatosSuplementarios (Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;    
    // Elimina un documento de los datos suplementarios del tipo fichero definido a nivel de tramite     
    public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;    
    // Obtiene el contenido de un determinado documento asociado a una anotación de registro
    public Documento getDocumentoRegistro(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Elimina un determinado documento de registro asociado a una anotación de registro
    public boolean eliminarDocumentoRegistro(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;    
    // Elimina todos los documentos grabados en campos suplementarios de tipo fichero definidos a nivel de trámite
    public boolean eliminarTodosDocumentosDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;

    // Elimina todos los documentos relacionados con un trámite
    public boolean eliminarTodosDocumentosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;
    
    // Permite recuperar el contenido de un documento de registro desde la pantalla de consulta de la anotación en el buzón de entrada
    public Documento getDocumentoRegistroConsulta(Documento doc,String origen) throws AlmacenDocumentoTramitacionException;
    
    // Permite almacenar un documento externo asociado a una notificación electrónica
    public boolean setDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException;
    // Elimina un documento externo asociado a una notificación electrónica
    public boolean eliminarDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException;
    
    // Permite recuperar la firma de un documento externo asociado a una notificación electrónica
    public String getFirmaDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException;
    
    // Duplica la informacion de un documento de tramitacion
    public void setDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException;
    
    // Elimina documento duplicado en caso de fallo 
    public void eliminarDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException;
}
