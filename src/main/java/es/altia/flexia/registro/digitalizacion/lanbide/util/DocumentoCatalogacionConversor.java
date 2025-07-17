package es.altia.flexia.registro.digitalizacion.lanbide.util;

import es.altia.agora.business.registro.DocumentoMetadatosVO;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.MetadatoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.TipoDocumentalCatalogacionVO;
import java.util.Vector;

/**
 * Convierte en objeto DocumentoCatalogacionVO diferentes objetos usados para almacenar datos de documentos
 * @author MilaNP
 */
public class DocumentoCatalogacionConversor {
    
    public static DocumentoCatalogacionVO fromRegistroVO(RegistroValueObject docRegistro){
         DocumentoCatalogacionVO docCatalogacion = new DocumentoCatalogacionVO();
         
         docCatalogacion.setDepartamento(docRegistro.getIdentDepart());
         docCatalogacion.setUnidadOrg(docRegistro.getUnidadOrgan());
         docCatalogacion.setEjercicio(docRegistro.getAnoReg());
         docCatalogacion.setNumeroAnot(docRegistro.getNumReg());
         docCatalogacion.setTipoAnot(docRegistro.getTipoReg());
         docCatalogacion.setNomDocumento(docRegistro.getNombreDoc());
         
         return docCatalogacion;
     }
    
    public static DocumentoMetadatosVO toDocMetadatosVO(DocumentoCatalogacionVO docCatalog){
        DocumentoMetadatosVO docMetadato = new DocumentoMetadatosVO();
        String metadatoId = null, metadatoValor = null;
        
        docMetadato.setDepartamento(docCatalog.getDepartamento());
        docMetadato.setUor((int) docCatalog.getUnidadOrg());
        docMetadato.setEjercicio(docCatalog.getEjercicio());
        docMetadato.setNumero(docCatalog.getNumeroAnot());
        docMetadato.setTipoRegistro(docCatalog.getTipoAnot());
        docMetadato.setNombreDoc(docCatalog.getNomDocumento());
        docMetadato.setIdDocumento(docCatalog.getIdDocumento());
        docMetadato.setTipoDocumental(docCatalog.getTipDocId());
        metadatoId = docCatalog.getMetadatoId();
        if(metadatoId!=null){
            docMetadato.setMetadatoId(metadatoId);
        }else{
            docMetadato.setMetadatoId("");
        }
        metadatoValor = docCatalog.getMetadatoValor();
        if(metadatoValor!=null){
            docMetadato.setMetadatoValor(metadatoValor);
        }else{
            docMetadato.setMetadatoValor("");
        }
        
        return docMetadato;
    }
    
    public static DocumentoCatalogacionVO fromDocMetadatosVO(DocumentoMetadatosVO docMetadato){
        DocumentoCatalogacionVO docCatalog = new DocumentoCatalogacionVO();
        TipoDocumentalCatalogacionVO tipoDocumental = new TipoDocumentalCatalogacionVO();
        MetadatoCatalogacionVO metadato = new MetadatoCatalogacionVO();
        
        docCatalog.setDepartamento(docMetadato.getDepartamento());
        docCatalog.setUnidadOrg(docMetadato.getUor());
        docCatalog.setEjercicio(docMetadato.getEjercicio());
        docCatalog.setNumeroAnot(docMetadato.getNumero());
        docCatalog.setTipoAnot(docMetadato.getTipoRegistro());
        docCatalog.setNomDocumento(docMetadato.getNombreDoc());
        docCatalog.setIdDocumento(docMetadato.getIdDocumento());
        tipoDocumental.setIdentificador(docMetadato.getTipoDocumental());
        docCatalog.setTipoDocumental(tipoDocumental);
        metadato.setIdMetadato(docMetadato.getMetadatoId());
        metadato.setValorMetadato(docMetadato.getMetadatoValor());
        metadato.setIdTipoDoc(docMetadato.getTipoDocumental());
        docCatalog.setMetadato(metadato);
        
        return docCatalog;
    }
    
    public static DocumentoCatalogacionVO fromInterfaceDocumento(Documento documento){
        DocumentoCatalogacionVO docCatalog = new DocumentoCatalogacionVO();
        docCatalog.setIdDocumento(documento.getIdDocumento());
        docCatalog.setDepartamento(documento.getCodigoDepartamento());
        docCatalog.setUnidadOrg(documento.getCodigoUnidadOrganica());
        docCatalog.setEjercicio(documento.getEjercicioAnotacion());
        docCatalog.setNumeroAnot(documento.getNumeroRegistro());
        docCatalog.setTipoAnot(documento.getTipoRegistro());
        docCatalog.setNomDocumento(documento.getNombreDocumento());
        
        return docCatalog;
    }
    
    /**
     * Convertimos el vector de RegistroValueObject en un DocumentoValueObject[] 
     * @param documentos
     * @return 
     */
    public static DocumentoValueObject[] toDocumentoVOArray(Vector<RegistroValueObject> documentos){
        DocumentoValueObject[] docAnotacion = new DocumentoValueObject[documentos.size()];
        for(int i=0;i<documentos.size();i++){
            RegistroValueObject registro = (RegistroValueObject)documentos.get(i);
            DocumentoValueObject doc = new DocumentoValueObject();
            doc.setNombre(registro.getNombreDoc());
            doc.setExtension(registro.getTipoDoc());
            doc.setFichero(registro.getDoc());
            doc.setFecha(registro.getFechaDoc());
            doc.setCatalogado(registro.getCatalogado());
            doc.setDigitalizado(registro.getCompulsado());
            doc.setUnidadOrg(registro.getUnidadOrgan());
            doc.setTipoDocumental(registro.getDescripcionTipoDocumental());
            doc.setIdDocumento(registro.getIdDocumento());
            doc.setObservDoc(registro.getObservDoc());
            docAnotacion[i] = doc;
        }
        return docAnotacion;
    }
}
