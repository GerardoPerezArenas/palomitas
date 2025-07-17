 package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.manager.visorregistro.VisorRegistroManager;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.ExpedienteOtroDocumentoManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.sge.persistence.manual.DocumentosExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.DocumentosRelacionExpedientesDAO;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoBBDD;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirmaBBDD;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Implementación del plugin de almacenamiento de documentos de tramitación en base de datos
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoBBDDImpl implements AlmacenDocumento,Serializable {
    private Logger log = Logger.getLogger(AlmacenDocumentoBBDDImpl.class);
    private String tipoPlugin=null;
    
     public String getNombreServicio(){
         return ConstantesDatos.PLUGIN_ALMACENAMIENTO_BBDD;
     }
     
           
    public boolean setDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
         boolean exito = false;
         log.debug(this.getClass().getName() + ".setDocumento init");

        DocumentoBBDD docBD =  (DocumentoBBDD)doc;
        String[] params = docBD.getParams();

        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codMunicipio", Integer.toString(docBD.getCodMunicipio()));
        gVO.setAtributo("codProcedimiento",docBD.getCodProcedimiento());
        gVO.setAtributo("ejercicio",Integer.toString(docBD.getEjercicio()));
        gVO.setAtributo("codTramite",Integer.toString(docBD.getCodTramite()));
        gVO.setAtributo("ocurrenciaTramite",Integer.toString(docBD.getOcurrenciaTramite()));
        gVO.setAtributo("codDocumento",Integer.toString(docBD.getCodDocumento()));
        gVO.setAtributo("codUsuario",Integer.toString(docBD.getCodUsuario()));
        gVO.setAtributo("nombreDocumento",docBD.getNombreDocumento());
        gVO.setAtributo("numeroDocumento",docBD.getNumeroDocumento());
        gVO.setAtributo("ficheroWord",docBD.getFichero());

        MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();
        metadatos.setId(docBD.getIdMetadatoDocumento());
        metadatos.setCsv(docBD.getMetadatoDocumentoCsv());
        metadatos.setCsvAplicacion(docBD.getMetadatoDocumentoCsvAplicacion());
        metadatos.setCsvUri(docBD.getMetadatoDocumentoCsvUri());
        gVO.setAtributo("metadatosDocumento", metadatos);
        gVO.setAtributo("insertarMetadatosEnBBDD", docBD.isInsertarMetadatosEnBBDD());

        if(!docBD.isDocRelacion()){
            // No se trata de un documento de una relación de expediente
            gVO.setAtributo("numeroExpediente",docBD.getNumeroExpediente());
            try{
                int resultado = DocumentosExpedienteManager.getInstance().grabarDocumento(gVO, params);
                if(resultado>0)
                    exito = true;

            }catch(Exception e){
                e.printStackTrace();
                log.debug(this.getClass().getName() + ".setDocumento: " + e.getMessage());
                throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitación " + docBD.getNombreDocumento() + "en BBDD: " + e.getMessage());
            }
        }else{
            // Se trata de un documento de una relación entre expedientes
            gVO.setAtributo("opcionGrabar", docBD.getOpcionGrabar());
            gVO.setAtributo("numeroRelacion", docBD.getNumeroRelacion());
            DocumentosRelacionExpedientesManager.getInstance().grabarDocumento(gVO, params);
            exito = true;
        }

         return exito;
    }


    public byte[] getDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;        
        GeneralValueObject gVO = new GeneralValueObject();

        DocumentoBBDD docBD = (DocumentoBBDD)doc;

        gVO.setAtributo("codMunicipio", Integer.toString(docBD.getCodMunicipio()));
        gVO.setAtributo("codTramite",Integer.toString(docBD.getCodTramite()));
        gVO.setAtributo("ocurrenciaTramite",Integer.toString(docBD.getOcurrenciaTramite()));
        gVO.setAtributo("numeroDocumento",docBD.getNumeroDocumento());
        gVO.setAtributo("expHistorico", docBD.isExpHistorico()?"true":"false");
        

        if(!docBD.isDocRelacion()){
            // Si el documento no pertenece a una relación de expedientes
            gVO.setAtributo("numeroExpediente",docBD.getNumeroExpediente());
            contenidoDocumento =  DocumentosExpedienteManager.getInstance().loadDocumento(gVO, docBD.getParams());
        }else{
            // Si el documento pertenece a una relación de expedientes
            gVO.setAtributo("numeroRelacion",docBD.getNumeroRelacion());
            contenidoDocumento = DocumentosRelacionExpedientesManager.getInstance().loadDocumento(gVO,docBD.getParams());
        }
        return contenidoDocumento;
    }

    
    // Se recupera el contenido de un documento de tramitación pero se le pasa la conexión de BBDD
    public byte[] getDocumento(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;        
        GeneralValueObject gVO = new GeneralValueObject();

        try{
            DocumentoBBDD docBD = (DocumentoBBDD)doc;

            gVO.setAtributo("codMunicipio", Integer.toString(docBD.getCodMunicipio()));
            gVO.setAtributo("codTramite",Integer.toString(docBD.getCodTramite()));
            gVO.setAtributo("ocurrenciaTramite",Integer.toString(docBD.getOcurrenciaTramite()));
            gVO.setAtributo("numeroDocumento",docBD.getNumeroDocumento());

            if(!docBD.isDocRelacion()){
                // Si el documento no pertenece a una relación de expedientes
                gVO.setAtributo("numeroExpediente",docBD.getNumeroExpediente());            
                contenidoDocumento = DocumentosExpedienteDAO.getInstance().loadDocumento(gVO,con);
            }else{
                // Si el documento pertenece a una relación de expedientes
                gVO.setAtributo("numeroRelacion",docBD.getNumeroRelacion());
                contenidoDocumento = DocumentosRelacionExpedientesDAO.getInstance().loadDocumento(gVO,con);
            }
        }catch(Exception e){
            throw new AlmacenDocumentoTramitacionException(2,"Error al recuperar el contenido del documento de tramitación de BBDD: " + e.getMessage());
        }
        return contenidoDocumento;        
    }
    

    public boolean eliminarDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
         DocumentoBBDD docBD = (DocumentoBBDD)doc;
         
        try{

            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
            tEVO.setCodMunicipio(Integer.toString(docBD.getCodMunicipio()));
            tEVO.setCodProcedimiento(docBD.getCodProcedimiento());
            tEVO.setEjercicio(Integer.toString(docBD.getEjercicio()));
            tEVO.setCodTramite(Integer.toString(docBD.getCodTramite()));
            tEVO.setOcurrenciaTramite(Integer.toString(docBD.getOcurrenciaTramite()));
            tEVO.setCodDocumento(Integer.toString(docBD.getCodDocumento()));
            
            if(!docBD.isDocRelacion()){
                tEVO.setNumeroExpediente(docBD.getNumeroExpediente());
                // El documento a eliminar no pertenece a una relación
                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docBD.getParams());
                exito = true;
            }else{
                tEVO.setNumeroRelacion(docBD.getNumeroRelacion());
                // El documento a eliminar pertenece a una relación
                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docBD.getParams());
                if(resultado>0) exito = true;
            }            

        }catch(Exception e){
            e.printStackTrace();
           log.debug(this.getClass().getName() + ".eliminarDocumento: " + e.getMessage());
           throw new AlmacenDocumentoTramitacionException(2,"Error al eliminar el documento de tramitación " + docBD.getNombreDocumento() + "en BBDD: " + e.getMessage());
        }

        return exito;
    }

    /** Este método no se implementa en el plugin de base de datos */
    public boolean setFirmaDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        return true;
    }


    public boolean isPluginGestor(){
        return false;
    }


    public boolean setDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
       
        DocumentoBBDD docBD = (DocumentoBBDD)doc;
        String[] params =  docBD.getParams();

        try{
            int codMunicipio          = docBD.getCodMunicipio();
            int ejercicio                = docBD.getEjercicio();
            String numExpediente = docBD.getNumeroExpediente();
            byte[] fichero             = docBD.getFichero();
            String tipoMime          = docBD.getTipoMimeContenido();
            String nombreDoc      = docBD.getNombreDocumento();
            String extension         = docBD.getExtension();

            ExpedienteOtroDocumentoVO docExterno = new ExpedienteOtroDocumentoVO();
            docExterno.setMunicipio(Integer.toString(codMunicipio));
            docExterno.setEjercicio(Integer.toString(ejercicio));
            docExterno.setNumeroExpediente(numExpediente);
            docExterno.setContenidoDocumento(fichero);
            docExterno.setTipoDocumento(tipoMime);
            docExterno.setNombreDocumento(nombreDoc);
            docExterno.setExtension(extension);
            
            int codDocumento = ExpedienteOtroDocumentoManager.getInstance().altaDocumento(docExterno, params);
            if(codDocumento==-1)
                throw new AlmacenDocumentoTramitacionException(11,"Error al grabar el documento externo en BBDD");

            log.debug(this.getClass().getName() + ".setDocumentoExterno código del documento insertado: " + codDocumento);
            exito = true;
        }catch(AlmacenDocumentoTramitacionException e){
            throw e;
        }
        catch(Exception e){
            throw new AlmacenDocumentoTramitacionException(11,"Error al grabar el documento externo en BBDD");
        }

        return exito;
    }

    /**
     * Sobreescribe el fichero del documento con el mismo fichero convertido a PDF
     * y con el CSV incrustado
     * 
     * @param doc
     * @return
     * @throws AlmacenDocumentoTramitacionException 
     */
    public boolean setDocumentoExternoFicheroCSV(Documento doc) throws AlmacenDocumentoTramitacionException {
        boolean exito = false;
        DocumentoBBDD docBD = (DocumentoBBDD) doc;
        String[] params = docBD.getParams();

        try {
            String municipio = String.valueOf(docBD.getCodMunicipio());
            String ejercicio = String.valueOf(docBD.getEjercicio());
            String numeroexpediente = docBD.getNumeroExpediente();
            String nombreDoc = docBD.getNombreDocumento();
            byte[] fichero = docBD.getFichero();
            String tipoMime = docBD.getTipoMimeContenido();
            String extension = docBD.getExtension();
            String codDocumento = String.valueOf(docBD.getCodDocumento());
            Long idMetadato = docBD.getIdMetadatoDocumento();
            String csv = docBD.getMetadatoDocumentoCsv();
            String csvAplicacion = docBD.getMetadatoDocumentoCsvAplicacion();
            String csvUri = docBD.getMetadatoDocumentoCsvUri();

            // Rellenamos los datos necesarios del documento
            ExpedienteOtroDocumentoVO docExterno = new ExpedienteOtroDocumentoVO();
            docExterno.setMunicipio(municipio);
            docExterno.setEjercicio(ejercicio);
            docExterno.setNumeroExpediente(numeroexpediente);
            docExterno.setNombreDocumento(nombreDoc);
            docExterno.setContenidoDocumento(fichero);
            docExterno.setTipoDocumento(tipoMime);
            docExterno.setExtension(extension);
            docExterno.setCodigoDocumento(codDocumento);
            docExterno.setIdMetadato(idMetadato);

            // Rellenamos los metadatos
            MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();
            metadatos.setId(idMetadato);
            metadatos.setCsv(csv);
            metadatos.setCsvAplicacion(csvAplicacion);
            metadatos.setCsvUri(csvUri);

            exito = ExpedienteOtroDocumentoManager.getInstance().modificaDocumentoCSV(docExterno, metadatos, params);
        } catch (Exception e) {
            log.error(e);
            throw new AlmacenDocumentoTramitacionException(11, "Error al grabar el documento externo en BBDD");
        }

        return exito;
    }
    
    public Documento getDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        DocumentoBBDD docBD = (DocumentoBBDD)doc;
        int codMunicipio    = docBD.getCodMunicipio();
        String numExped  = docBD.getNumeroExpediente();
        String codDocumento = docBD.getNumeroDocumento();
        int ejercicio          = docBD.getEjercicio();
        String[] params   = docBD.getParams();

        ExpedienteOtroDocumentoVO docExterno = ExpedienteOtroDocumentoManager.getInstance().getDocumento(codDocumento,
                Integer.toString(ejercicio),Integer.toString(codMunicipio), numExped, docBD.isExpHistorico(), params);
        docBD.setFichero(docExterno.getContenidoDocumento());
        docBD.setNombreDocumento(docExterno.getNombreDocumento());
        docBD.setTipoMimeContenido(docExterno.getTipoDocumento());

        return docBD;
    }

    public boolean eliminarDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoBBDD docBD = (DocumentoBBDD)doc;

        int codMunicipio    = docBD.getCodMunicipio();
        int ejercicio          = docBD.getEjercicio();
        String numExp      = docBD.getNumeroExpediente();
        String numeroDocumento =  docBD.getNumeroDocumento();
        String[] params    = docBD.getParams();

        exito =  ExpedienteOtroDocumentoManager.getInstance().eliminaDocumento(Integer.toString(codMunicipio),Integer.toString(ejercicio), numExp,numeroDocumento, params);
        if(!exito)
            throw new AlmacenDocumentoTramitacionException(12,"Error al eliminar el documento de BBDD");

        return exito;
    }


    /**
     *  Alta del binario correspondiente a un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a almacenar
     * @return: Un boolean
     */
    public boolean setDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        
        if(!doc.isModificarAdjuntoDocExpediente()) // Se da de alta el adjunto asociado al expediente
            exito = ExpedienteDocPresentadoManager.getInstance().setDocumentoPresentado(doc);
        else // Se modifica el contenido del adjunto
            exito = ExpedienteDocPresentadoManager.getInstance().modificarDocumentoPresentado(doc);

        return exito;
    }

    /**
     *  Recupera el contenido de un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a recuperar
     * @return: Documento
     */
    public Documento getDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        Documento salida = null;
        salida = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
        return salida;
    }

    /**
     * Elimina un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a eliminar
     * @return: Un boolean
     */
    public boolean eliminarDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        exito = ExpedienteDocPresentadoManager.getInstance().eliminarDocumentoPresentado(doc);
        return exito;
    }


    // Recupera el contenido de un documento ajeno al sistema enviado al portafirmas para que sea firmado
    public DocumentoOtroFirmaVO getDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{
        DocumentoOtroFirmaVO salida = null;
        String[] params = doc.getParams();        
        salida = EDocExtPortafirmasManager.getInstance().getDocumento(Integer.toString(doc.getCodigoDocumento()), params);
        return salida;
    }

    // Da de alta un documento ajeno al sistema para que pueda ser firmado en el portafirmas
    public boolean setFirmaDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{
        // Se delega la responsabilidad en el método guardarFirma de la clase EDocExtPortafirmasManager
        return EDocExtPortafirmasManager.getInstance().guardarFirma(doc, doc.getParams());        
    }

    //public boolean setFirmaDocumentoExpediente(DocumentoProcedimientoFirmaVO doc) throws AlmacenDocumentoTramitacionException {
    public boolean setFirmaDocumentoExpediente(Documento doc) throws AlmacenDocumentoTramitacionException {                
        boolean exito = false;
        
        try{
            DocumentoProcedimientoFirmaVO documento = new DocumentoProcedimientoFirmaVO();
            documento.setFirma(doc.getFichero());
            documento.setIdPresentado(doc.getCodDocumento());
            documento.setCodigoUsuarioFirma(Integer.toString(doc.getCodUsuario()));
            documento.setFechaFirma(Calendar.getInstance());
            documento.setCodOrganizacion(Integer.toString(doc.getCodMunicipio()));        
            documento.setCodigoDocumento(Integer.parseInt(doc.getNumeroDocumento()));
            documento.setParams(doc.getParams());        
            documento.setObservaciones(doc.getObservaciones());                
            documento.setIdNumFirma(doc.getIdFirma());
                        
            exito = DocsProcedimientoPortafirmasManager.getInstance().guardarFirma(documento, doc.getParams());
            
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de expediente: " + e.getMessage());           
        }
        
        return exito;
    }

    
    public boolean setFirmaDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{  
        boolean exito = false;        
        
        try{
            ResourceBundle config = ResourceBundle.getBundle("Portafirmas");
            DocumentoBBDD docBD = (DocumentoBBDD)doc;
            
            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();            
            adjunto.setFirma(new String(docBD.getFichero()));            
            adjunto.setEstadoFirma(ConstantesDatos.ESTADO_FIRMA_FIRMADO);            
            String plataformaFirma = config.getString(docBD.getCodMunicipio()  + "/PluginPortafirmas");
            adjunto.setPlataformaFirma(plataformaFirma);
            adjunto.setCodUsuarioFirmaOtro(docBD.getCodUsuario());
            adjunto.setTipoCertificadoFirma(ConstantesDatos.TIPO_CERTIFICADO_USUARIO);
            adjunto.setIdDocExterno(Integer.parseInt(docBD.getNumeroDocumento()));
            adjunto.setCodigoNotificacion(docBD.getCodDocumento());
            adjunto.setFechaFirma(Calendar.getInstance());
            exito = AdjuntoNotificacionManager.getInstance().setFirmaAdjuntoExternoNotificacion(adjunto,doc.getParams());        
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento externo de una notificación: " + e.getMessage());                   
        }
        
        return exito;
    }
 
    
    public Documento getDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        
        try{         
            AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getDocumentoExternoNotificacion(doc.getCodDocumento(),doc.isExpHistorico(),con);

            if(adjunto!=null){
                String nombre = adjunto.getNombre();
                int index = nombre.lastIndexOf(".");            
                doc.setFichero(adjunto.getContenido());
                doc.setNombreDocumento(nombre.substring(0,index));
                doc.setTipoMimeContenido(adjunto.getContentType());            
                doc.setExtension(nombre.substring(index +1,nombre.length()));
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(2,"Error al recuperar el contenido del doucmento externo de una notificación de la BBDD: " + e.getMessage());
        }
        
        return doc;
        
    }
    
    public boolean setDocumentoDatoSuplementarioExpediente(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
         
        try{            
            exito = DatosSuplementariosDAO.getInstance().grabarDocumentoDatosSuplementarioFicheroExpediente(doc,con);

        }catch(TechnicalException e){
            log.error("setDocumentoDatoSuplementarioExpediente() - Error al dar de alta un documento  en un campo suplementario fichero de expediente: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,"Error al dar de alta un documento  en un campo suplementario fichero de expediente: " + e.getMessage());  
        }           

        return exito;        
    }
    
     
   
    
    /**
     * Graba un documento en un campo suplementario definido a nivel de trámite
     * @param doc: Objeto de tipo Documento con la información del documento a dar de alta
     * @param con: Conexión a la BBDD
     * @return True si se ha insertado el documento y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException: Si ocurre algún error
     */
    public boolean setDocumentoDatoSuplementarioTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{         
        boolean exito = false;
        
        try{            
            exito = DatosSuplementariosDAO.getInstance().grabarDocumentoDatoSuplementarioTramite(doc, con);
            
            
        }catch(Exception e){
            log.error("setDocumentoDatosSuplementarioTramite() - Error al dar de alta uns documentos en un campo suplementario tipo fichero en un trámite: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,e.getMessage());   
        }   
        return exito;
    }
 
    
    
    public void setDocumentosRegistro(ArrayList<Documento> documentos,Connection con) throws AlmacenDocumentoTramitacionException{
        
        AnotacionRegistroDAO dao = AnotacionRegistroDAO.getInstance();
        // Se han eliminado los documentos de la anotación de registro => Se procede a invocar el plugin de
        // almacenamiento de documentos.    
        try{
            dao.insertarDocsRegistro(con,documentos);
            
        }catch(AnotacionRegistroException e){
            log.error("setDocumentosRegistro error al insertarl os documentos de la notacion de registro en base de datos: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al dar de alta el documento de registro en base de datos: " + e.getMessage());
        }
    }
    
    
    
    private RegistroValueObject documentoToRegistroValueObject(Documento doc){
        RegistroValueObject salida = null;
        
        try{
            if(doc!=null && doc.isDocumentoRegistro()){                
                salida = new RegistroValueObject();                
                salida.setIdentDepart(doc.getCodigoDepartamento());                
                salida.setUnidadOrgan(doc.getCodigoUnidadOrganica());
                salida.setAnoReg(doc.getEjercicioAnotacion());
                salida.setNumReg(doc.getNumeroRegistro());
                salida.setTipoReg(doc.getTipoRegistro());
                salida.setNombreDoc(doc.getNombreDocumento());
                salida.setTipoDoc(doc.getTipoDocumento());                
                salida.setFechaDoc(doc.getFechaDocumento());
                salida.setEntregado(doc.getEntregado());                
            }            
        }catch(Exception e){
            log.error("documentoToRegistroValueObject() error al realizar la conversión de objeto: " + e.getMessage());
        }        
        return salida;
    }
    
    
    
    public Documento getDocumentoRegistro(Documento doc) throws AlmacenDocumentoTramitacionException{
        
        Connection con = null;
        
        if(doc!=null){
            String[] params = doc.getParams();
            
            try{
                AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                                
                DocumentoAnotacionRegistroVO docAnotacion = new DocumentoAnotacionRegistroVO();    
                docAnotacion.setCodDepartamento(doc.getCodigoDepartamento());
                docAnotacion.setCodigoUorRegistro(doc.getCodigoUnidadOrganica());
                docAnotacion.setEjercicio(doc.getEjercicioAnotacion());
                docAnotacion.setNumeroAnotacion((int)doc.getNumeroRegistro());
                docAnotacion.setTipoEntrada(doc.getTipoRegistro());
                docAnotacion.setNombreDocumento(doc.getNombreDocumento());
                
                docAnotacion = AnotacionRegistroDAO.getInstance().getDocumentoAnotacionRegistro(docAnotacion,con);                                
                doc.setFichero(docAnotacion.getContenido());
                doc.setTipoMimeContenido(docAnotacion.getTipoDocumento());
                doc.setIdDocGestor(docAnotacion.getIdDocGestor());
                
            }catch(BDException e){
                log.error("getDocumentoRegistro(): Error al obtener una conexión a la BBDD: " + e.getMessage());
                throw new AlmacenDocumentoTramitacionException(0,"Error al obtener una conexión a la BBDD: " + e.getMessage());
            }finally{                
                try{
                    if(con!=null) con.close();
                }catch(SQLException e){
                    log.error("AnotacionRegistroDAO.getDocumentoRegistro(): Error al cerrar conexión a la BBDD: " + e.getMessage());
                }
            }
        }
        return doc;
    }
    
   

    // Recupera un documento de los datos suplementarios de tipo fichero definido a nivel de expediente
    public Documento getDocumentoDatosSuplementarios(Documento doc) 
            throws AlmacenDocumentoTramitacionException{
        
        try{
            
            CampoSuplementarioFicheroVO campo = DatosSuplementariosManager.getInstance().getCampoSuplementarioFicheroExpediente(doc.getCodTipoDato(),
                    String.valueOf(doc.getCodMunicipio()),String.valueOf(doc.getEjercicio()),doc.getNumeroExpediente(),
                    doc.isExpHistorico(), doc.getParams());

            doc.setFichero(campo.getContenido());
            doc.setNombreDocumento(campo.getNombreFichero());
            doc.setTipoMimeContenido(campo.getTipoMime()); 
        
       }catch(TechnicalException e){
            log.error("getDocumentosDatosSuplementarios(): " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,e.getMessage());
        }
        
        return doc;        
    }

    // Recupera un documento de los datos suplementarios del tipo fichero definido a nivel de tramite
    public Documento getDocumentoDatosSuplementariosTramite(Documento doc) 
            throws AlmacenDocumentoTramitacionException{
        
        try{
            
            CampoSuplementarioFicheroVO campo = DatosSuplementariosManager.getInstance().getCampoSuplementarioFicheroTramite(doc.getCodTipoDato(),
                    String.valueOf(doc.getCodMunicipio()),String.valueOf(doc.getEjercicio()),doc.getNumeroExpediente(),
                    doc.getCodTramite(),doc.getOcurrenciaTramite(),doc.isExpHistorico(),doc.getParams());            
            
            doc.setFichero(campo.getContenido());
            doc.setNombreDocumento(campo.getNombreFichero());
            doc.setTipoMimeContenido(campo.getTipoMime());
            
        
       }catch(TechnicalException e){
            log.error("getDocumentosDatosSuplementariosTramite(): " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,e.getMessage());
        }
        
        return doc;
        
    }
    
    // Elimina un documento de los datos suplementarios del tipo fichero definido a nivel de expediente
    public boolean eliminarDocumentoDatosSuplementarios(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{        
        boolean exito = false;       
        
        try{
            
            DocumentoBBDD docBD = (DocumentoBBDD)doc;
            // Se elimina el registro asociado al documento de la base de datos
            exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementarios(docBD, con);
           
                
        }catch(TechnicalException e){
            log.error("eliminarDocumentoDatosSuplementarios(): " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,e.getMessage());
        }

        return exito;
        
    }
        
    // Elimina un documento de los datos suplementarios del tipo fichero definido a nivel de tramite    
    public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{        
        boolean exito = false;       
        
        try{                        
            exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementariosTramite(doc, con);                    
            
        }catch(TechnicalException e){
            log.error("eliminarDocumentoDatosSuplementariosTramite(): " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,e.getMessage());
        }

        return exito;
        
    }
    

    /**
      * Elimina un documento asociado a una anotación de registro del gestor documental Alfresco
      * @param doc: Objeto de tipo DocumentoBBDD que contiene la información necesaria del documento para poder eliminarlo
      * @param con: Conexión a la BBDD
      * @throws AlmacenDocumentoTramitacionException: Si ocurre algún error
      */
    public boolean eliminarDocumentoRegistro(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
              
        try{                   
            AnotacionRegistroDAO.getInstance().eliminarDocumentoRegistro(doc,con);
            exito = true;         
        }catch(AnotacionRegistroException e){
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar un documento asociado a una anotación de registro: " + e.getMessage());
        }
        
        return exito;
    }

 
    
    /**
     * 
     * @param doc: Objeto de tipo Documento que contiene los datos del trámite y expediente para el 
     * cual, hay que eliminar el contenido de sus campos suplementarios de tipo fichero
     * @param con: Conexión a la BBDD
     * @return True si se ha ejecutado correctamente y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException: Si ocurre algún error
     */
    public boolean eliminarTodosDocumentosDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        
        try{                   
            DatosSuplementariosDAO.getInstance().eliminarTodosDocumentoDatosSuplementariosTramite(doc, con);
            exito = true;         
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar todos los archivos almacenados en campos suplementarios de tipo fichero de un determinado trámite: " + e.getMessage());
        }
        
        return exito;
    }
    
 
    
    public Documento getDocumentoRegistroConsulta(Documento doc,String origen) throws AlmacenDocumentoTramitacionException{
        
        DocumentoBBDD docBD = (DocumentoBBDD)doc;
        RegistroValueObject reg = new RegistroValueObject();        
        String[] params = null;
        try{
            String codigo = null;            
            params = docBD.getParams();
            
            if("REC".equals(origen)){          
              // Se crea el objeto RegistroValueObject       
              reg.setIdOrganizacion(docBD.getCodMunicipio());
              reg.setNumReg(docBD.getNumeroRegistro());
              reg.setAnoReg(docBD.getEjercicio());
              reg.setUnidadOrgan(docBD.getCodigoUnidadOrganica());
              reg.setIdServicioOrigen(origen); // Tipo de servicio de origen (SGE,REC,Pisa ...)
            }
            else{

              reg.setNombreDoc(docBD.getNombreDocumento());
              reg.setTipoDoc(docBD.getTipoMimeContenido());
              reg.setFechaDoc(docBD.getFechaDocumento());
              reg.setIdOrganizacion(docBD.getCodMunicipio());
              reg.setNumReg(doc.getNumeroRegistro());
              reg.setAnoReg(doc.getEjercicioAnotacion());          
              reg.setUnidadOrgan(docBD.getCodigoUnidadOrganica());
              reg.setIdServicioOrigen(origen); // Tipo de servicio de origen (SGE,REC,Pisa ...)
              reg.setTipoReg(docBD.getTipoRegistro());
              codigo = "0"; // Se pone código a cero puesto que para otras implementaciones(SGE,PIST,PISA ...) no hay código para el documento a recuperar
            }

            DocumentoValueObject docRegistro = VisorRegistroManager.getInstance().viewDocument(reg,Integer.parseInt(codigo), params);      
            byte[] fichero = docRegistro.getFichero();

            docBD.setFichero(fichero);
            docBD.setExtension(docRegistro.getExtension());
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1, "Error al recuperar el contenido del documento de registro: " + e.getMessage());
            
        }finally{
            return docBD;
        }
    }
    
    
    /**
     * Operación que da de alta un documento externo que se adjunta a una notificación electrónica que se envía
     * al finalizar un trámite de un determinado expediente
     * @param doc: Objeto que implementa la interfaz Documento
     * @return True si se ha podido dar de alta el documento y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException: Si ocurre un error
     */
    public boolean setDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        String[] params = null;
        try{
            
            DocumentoBBDD docBD = (DocumentoBBDD)doc;
            params = docBD.getParams();
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
            adjunto.setCodigoMunicipio(docBD.getCodMunicipio());
            adjunto.setCodigoTramite(docBD.getCodTramite());
            adjunto.setOcurrenciaTramite(docBD.getOcurrenciaTramite());
            adjunto.setNumeroExpediente(docBD.getNumeroExpediente());
            adjunto.setContenido(docBD.getFichero());
            adjunto.setNombre(docBD.getNombreDocumento() + ConstantesDatos.DOT + docBD.getExtension());
            adjunto.setContentType(docBD.getTipoMimeContenido());                
            adjunto.setCodigoNotificacion(docBD.getCodDocumento());
            
            exito = AdjuntoNotificacionDAO.getInstance().insertarAdjuntoExterno(adjunto, params[0], con);
            
        }catch(BDException e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al obtener una conexión a la BBDD al tratar de almacenar un documento externo asociado a una notificación electrónica: " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al obtener una conexión a la BBDD al tratar de almacenar un documento externo asociado a una notificación electrónica: " + e.getMessage());
        }finally{
            
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }            
        }
        return exito;
    }
    
    
    
    /**
     * Elimina un documento externo asociado a una notificación de la BBDD
     * @param doc: Objeto que implementa la interfaz Documento y que contiene la información del documento a eliminar
     * @return True siha ido todo bien y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException si ocurre algún error
     */
    public boolean eliminarDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
                
        try{            
            exito = AdjuntoNotificacionManager.getInstance().eliminarAdjuntoExterno(doc.getCodDocumento(),doc.getParams());
            
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(3,"Error al eliminar un documento externo asociado a una notificación: " + e.getMessage());
        }        
        return exito;
    }
    
    
    
    // Permite recuperar la firma de un documento externo asociado a una notificación electrónica
    public String getFirmaDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{         
        String firma = null;        
        DocumentoBBDD docBD = (DocumentoBBDD)doc;
                
        
        try{            
            firma = AdjuntoNotificacionDAO.getInstance().getFirmaAdjuntoExternoNotificacion(docBD.getCodDocumento(),con);
            
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(2,"Error al recuperar la firma de un documento externo de una notificación: " + e.getMessage());
        }
        return firma;        
    }
     
    /**
     * Guarda en base de datos, tabla E_CRD, de un documento duplicado (mismos metadatos y contenido) de un documento de tramitacion
     * @param doc Objeto de tipo interfaz DocumentoFirma, en este caso llega una instancia de la implementacion DocumentoFirmaBBDD de dicha interfaz
     * @param params
     * @throws AlmacenDocumentoTramitacionException 
     */
    
    public void setDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException {
        log.info("AlmacenDocumentoBBDDImpl.setDocumentoDuplicado()");
        
        try {
            DocumentosExpedienteManager.getInstance().duplicarDocumentoTramitacionConFirma(doc, params);
        } catch (SQLException sqle){
            sqle.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1, "Error al duplicar el documento");
        } catch (BDException ex) {
            ex.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(0, "Error de BBDD.");
        }
    }

    @Override
    public void eliminarDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException {
        log.info(("AlmacenDocumentoBBDDImpl.eliminarDocumentoDuplicado()"));
       
        DocumentoFirmaBBDD docBD = (DocumentoFirmaBBDD) doc;
        try{
            DocumentosExpedienteManager.getInstance().eliminarDocumentoTramitacionDuplicado(docBD, params);
        } catch (SQLException sqle){
            sqle.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1, "Error al eliminar documento duplicado");
        } catch (BDException ex){
            ex.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(0, "Error al obtener la conexión a la BBDD");
        }
    }
    
    /**
     * 
     * @param doc: Objeto de tipo Documento que contiene los datos del trámite y expediente para el 
     * cual, hay que eliminar los documentos asociados a ese tramite
     * @param con: Conexión a la BBDD
     * @return True si se ha ejecutado correctamente y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException: Si ocurre algún error
     */
    @Override
    public boolean eliminarTodosDocumentosTramite(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        log.debug("AlmacenDocumentoBBDDImpl.eliminarTodosDocumentosTramite() BEGIN ");
         boolean exito = false;
            
         try{
            DocumentosExpedienteDAO.getInstance().eliminarTodosDocumentosTramitacion(doc, con);
            exito = true; 
        } catch (SQLException sqle){
            sqle.printStackTrace();
            log.error("AlmacenDocumentoBBDDImpl.eliminarTodosDocumentosTramite() ERROR: " + sqle.getMessage());
            throw new AlmacenDocumentoTramitacionException(1, "Error al eliminar documento duplicado");
        }            

        log.debug("AlmacenDocumentoBBDDImpl.eliminarTodosDocumentosTramite() END exito = "+exito);
        return exito;
    }
     
}
