package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasDAO;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasDAO;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.ExpedienteOtroDocumentoManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.exception.TechnicalDocumentoException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * Plugin de almacenamiento de documentos en un gestor documental
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoAlfrescoImpl implements AlmacenDocumento,Serializable{
    private Logger log = Logger.getLogger(AlmacenDocumentoAlfrescoImpl.class);

    private final String GUION = "-";
    private final String GUION_BAJO = "_";
    private final String DOT    = ".";
    private final String CERO  = "0";
    private final String ELEMENTO_REEMPLAZAR = "@";
    private final String SUFIJO_EXT = "EXT";
    private String tipoPlugin = null;
        
    public String getNombreServicio(){
         return ConstantesDatos.PLUGIN_ALMACENAMIENTO_ALFRESCO;
     }
    

    /**
     * Almacen la firma de un documento. El documento previamente ya ha sido guardado en el gestor
     * @param doc: Objeto con los datos del documento a dar de alta
     * @return Un boolean
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException
     */
    public boolean setFirmaDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;

        DocumentoGestor docGestor =  (DocumentoGestor)doc;
        String[] params = docGestor.getParams();

        try{
           
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            Class implClass = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
           
            gestor.setDocumento(docGestor);            
            exito = true;
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(7,"Error al guardar la firma del documento en el gestor documental");
        }


        return exito;
    }

   
    /**
     * Devuelve el nombre que tendr� un document en el gestor documental
     * @param doc: Objeto con toda la informaci�n relativa al documento
     * @param nombre: Nombre del documento o fichero
     * @return Nombre del documento completo
     */
    private String getNombreFicheroCompleto(Documento doc,String nombre){

        StringBuffer nuevoNombreCompletoFichero = new StringBuffer();
        DocumentoGestor docGestor = (DocumentoGestor)doc;

         /**************** NUEVO NOMBRE DEL FICHERO A MODIFICAR *****/
        // Se establece el nuevo nombre que tendr� el documento en el gestor si es que se modifica
        int longitudNumeroDocumento = docGestor.getNumeroDocumento().length();
        int longitudOcuTramite = Integer.toString(docGestor.getOcurrenciaTramite()).length();        
        int longitudCodTramite = Integer.toString(docGestor.getCodTramite()).length();
        nuevoNombreCompletoFichero.append(nombre);
        nuevoNombreCompletoFichero.append(GUION);

        for(int i=0;i<(docGestor.getLongitudCodVisibleTramiteBD() - longitudCodTramite);i++){
            nuevoNombreCompletoFichero.append(CERO);
        }
        
        nuevoNombreCompletoFichero.append(docGestor.getCodTramite());        
        nuevoNombreCompletoFichero.append(GUION);
        for(int i=0;i<(docGestor.getLongitudNumeroDocumentoBD() - longitudNumeroDocumento);i++){
            nuevoNombreCompletoFichero.append(CERO);
        }
        nuevoNombreCompletoFichero.append(docGestor.getNumeroDocumento());
        nuevoNombreCompletoFichero.append(GUION);

        for(int i=0;i<(docGestor.getLongitudOcurrenciaTramiteInternoBD() - longitudOcuTramite);i++){
            nuevoNombreCompletoFichero.append(CERO);
        }
        nuevoNombreCompletoFichero.append(docGestor.getOcurrenciaTramite());
        nuevoNombreCompletoFichero.append(DOT);
        nuevoNombreCompletoFichero.append(docGestor.getExtension());
        /**************** FIN NUEVO NOMBRE DEL FICHERO A MODIFICAR *****/

        return nuevoNombreCompletoFichero.toString();
    }


    /**
     * Devuelve el nombre del fichero completo de un documento externo asociado a un determinado expediente
     * @param doc: Datos del documento
     * @param nombre: Nombre del fichero
     * @return Nombre del fichero completo externo
     */
     private String getNombreFicheroCompletoExterno(Documento doc,String nombre){

        StringBuffer nuevoNombreCompletoFichero = new StringBuffer();
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        // Se establece el nuevo nombre que tendr� el documento en el gestor si es que se modifica
        int longitudNumeroDocumento = docGestor.getNumeroDocumento().length();

        nuevoNombreCompletoFichero.append(this.SUFIJO_EXT);
        nuevoNombreCompletoFichero.append(GUION);
        nuevoNombreCompletoFichero.append(nombre);
        nuevoNombreCompletoFichero.append(GUION);
                
        for(int i=0;i<(docGestor.getLongitudNumeroDocumentoBD() - longitudNumeroDocumento);i++){
            nuevoNombreCompletoFichero.append(CERO);
        }
        nuevoNombreCompletoFichero.append(docGestor.getNumeroDocumento());
        nuevoNombreCompletoFichero.append(DOT);
        nuevoNombreCompletoFichero.append(docGestor.getExtension());
        
        return nuevoNombreCompletoFichero.toString();
    }


     /**
      * Reemplaza los caracteres especiales que se encuentran en el array indicado por el par�metro caracteres en
      * el String dato
      * @param dato: String que se sustituye
      * @param caracteres: Array con la lista de caracteres especiales a reemplazar si es necesario
      * @return String
      */
    private String sustituirCaracteresEspeciales(String dato,String[] caracteres){
        for(int i=0;caracteres!=null && i<caracteres.length;i++){
            dato = dato.replaceAll(Pattern.quote(caracteres[i].trim()),"");
        }
        return dato;
    }

 
    
    private String[] getListaCaracteresNoPermitidos(int codMunicipio){        
        String[] salida = null;               
        try{
            ResourceBundle documentosConfig = ResourceBundle.getBundle("documentos");
            String caracteresNoPermitidos = documentosConfig.getString("Almacenamiento/" + codMunicipio + "/ALFRESCO/caracteres_no_permitidos");

            if(caracteresNoPermitidos!=null && !"".equals(caracteresNoPermitidos))
                salida = caracteresNoPermitidos.split(ConstantesDatos.DOT_COMMA);
        }catch(Exception e){
            e.printStackTrace();
        }
        return salida;
    }
    
    
    

    public boolean setDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
         boolean exito = false;
         log.debug(this.getClass().getName() + ".setDocumento init");

        DocumentoGestor docGestor =  (DocumentoGestor)doc;
        String[] params = docGestor.getParams();

        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codMunicipio", Integer.toString(docGestor.getCodMunicipio()));
        gVO.setAtributo("codProcedimiento",docGestor.getCodProcedimiento());
        gVO.setAtributo("ejercicio",Integer.toString(docGestor.getEjercicio()));
        gVO.setAtributo("codTramite",Integer.toString(docGestor.getCodTramite()));
        gVO.setAtributo("ocurrenciaTramite",Integer.toString(docGestor.getOcurrenciaTramite()));
        gVO.setAtributo("codDocumento",Integer.toString(docGestor.getCodDocumento()));
        gVO.setAtributo("codUsuario",Integer.toString(docGestor.getCodUsuario()));
        gVO.setAtributo("nombreDocumento",docGestor.getNombreDocumento());
        gVO.setAtributo("numeroDocumento",docGestor.getNumeroDocumento());
        gVO.setAtributo("ficheroWord",docGestor.getFichero());
        gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());


        /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
        
        String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());
        
        if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
            ArrayList<String> carpetas = docGestor.getListaCarpetas();

            for(int i=0;carpetas!=null && i<carpetas.size();i++){
                String carpeta = carpetas.get(i);
                log.debug("carpeta: " + carpeta);
                carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                carpetas.set(i, carpeta);
            }// for
            docGestor.setListaCarpetas(carpetas);
        
        }// if
        
        CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
        docGestor.setImplClassGestor(cache.getImplClassGestor());
        docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
        docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
        docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
        docGestor.setPaswordGestor(cache.getPasswordGestor());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        
        
        /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
        if(!docGestor.isDocRelacion()){
                // No se trata de un documento de una relaci�n de expediente
                gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());
                if(docGestor.getNumeroDocumento()!=null && !"".equals(docGestor.getNumeroDocumento()))
                {
                    try{                        
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();

                        // Se recupera el nombre del documento que ten�a originalmente porque puede que sea modificado
                        String nombreOriginalDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);
                        String nombreNuevoDocumento = docGestor.getNombreDocumento();

                        // Nombre original  del documento
                        docGestor.setNombreDocumento(nombreOriginalDocumento);
                        docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento), listaCaracteresNoPermitidos));
                        // Nuevo nombre del fichero                        
                        docGestor.setNuevoNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreNuevoDocumento), listaCaracteresNoPermitidos));
                        
                        if(gestor.modificarDocumento(docGestor)){                        
                            // Se modifica el documento
                            DocumentosExpedienteManager.getInstance().grabarDocumentoGestor(gVO, params);
                            exito = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitaci�n " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                    }

                }else{

                    try{
                        log.debug("docGestor.getImplClassGestor(): " + docGestor.getImplClassGestor());
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                        // Se graba el documento por primera vez
                        // Se guardan los datos del documento en base de datos
                        int numeroDocumento = DocumentosExpedienteManager.getInstance().grabarDocumentoGestor(gVO, params);
                        if(numeroDocumento>0){
                            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));                           
                            docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));
                            gestor.setDocumento(docGestor);                            
                            exito = true;
                        }
                    }catch(Exception e){
                            exito = false;
                            e.printStackTrace();
                            log.debug(this.getClass().getName() + ".setDocumento: " + e.getMessage());
                            /******************  SI SE PRODUCE UN FALLO AL ALMACENAR EL DOCUMENTO EN EL GESTOR, HAY QUE ELIMINAR LA REFERENCIA AL DOCUMENTO EN EL GESTOR   ******************/
                            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                            tEVO.setCodMunicipio(Integer.toString(docGestor.getCodMunicipio()));
                            tEVO.setCodProcedimiento(docGestor.getCodProcedimiento());
                            tEVO.setEjercicio(Integer.toString(docGestor.getEjercicio()));
                            tEVO.setCodTramite(Integer.toString(docGestor.getCodTramite()));
                            tEVO.setOcurrenciaTramite(Integer.toString(docGestor.getOcurrenciaTramite()));
                            //tEVO.setCodDocumento(Integer.toString(docGestor.getCodDocumento()));
                            tEVO.setCodDocumento(docGestor.getNumeroDocumento());

                            if(!docGestor.isDocRelacion()){
                                tEVO.setNumeroExpediente(docGestor.getNumeroExpediente());
                                // El documento a eliminar no pertenece a una relaci�n
                                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                                exito = true;
                            }else{
                                // TODO: VERIFICAR CUANDO EL DOCUMENTO PERTENECE A UNA RELACI�N
                                tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                                // El documento a eliminar pertenece a una relaci�n
                                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                                if(resultado>0) exito = true;
                            }
                            /********************************************************/
                            throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitaci�n " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                     }//catch
                }// else
            }else{
                /***************  SI EL DOCUMENTO PERTENECE A UNA RELACI�N ENTRE EXPEDIENTES **********************/
               // No se trata de un documento de una relaci�n de expediente
                //gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());             
               gVO.setAtributo("opcionGrabar", docGestor.getOpcionGrabar());
               gVO.setAtributo("numeroRelacion", docGestor.getNumeroRelacion());
                
                if(docGestor.getNumeroDocumento()!=null && !"".equals(docGestor.getNumeroDocumento()))
                {
                    try{
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();

                        String nombreOriginalDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, params);
                        String nombreDocumentoNuevo = docGestor.getNombreDocumento();

                        // Nombre original  del documento
                        docGestor.setNombreDocumento(nombreOriginalDocumento);
                        // Nombre original del document en el gestor                       
                        docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento),listaCaracteresNoPermitidos));
                        // Nuevo nombre del fichero a modificar                        
                        docGestor.setNuevoNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreDocumentoNuevo),listaCaracteresNoPermitidos));
                                                
                        if(gestor.modificarDocumento(docGestor)){                        
                            // Se modifica el documento
                            DocumentosRelacionExpedientesManager.getInstance().grabarDocumentoGestor(gVO, params);
                            exito = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitaci�n " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                    }

                }else{

                    try{
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                        // Se graba el documento por primera vez
                        // Se guardan los datos del documento en base de datos
                        int numeroDocumento = DocumentosRelacionExpedientesManager.getInstance().grabarDocumentoGestor(gVO, params);
                        if(numeroDocumento>0){
                            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));                           
                            docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                           // Se guarda el documento en el gestor y se recupera la url del documento en el gestor
                           String url = gestor.setDocumento(docGestor);                            
                           // Para los documentos presentes en la relaci�n se compone  un nuevo fichero HTML con los expedientes relacionados.
                           ResourceBundle bundle = ResourceBundle.getBundle("documentos") ;
                           String cabeceraHTML = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_CABECERA_HTML_RELACION);
                           String cuerpoHTML    = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_CUERPO_HTML_RELACION);
                           String pieHTML         = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_PIE_HTML_RELACION);

                           StringBuffer contenidoHTML = new StringBuffer();                         
                           contenidoHTML.append(cabeceraHTML);
                           for(int i=0;docGestor.getListaExpedientes()!=null && i<docGestor.getListaExpedientes().size();i++){
                                String expediente = docGestor.getListaExpedientes().get(i);
                                log.debug(" ***************> Expediente a tratar: " + expediente);
                                String dExp = cuerpoHTML;                                
                                contenidoHTML.append(dExp.replaceFirst(ELEMENTO_REEMPLAZAR,expediente));
                                contenidoHTML.append(pieHTML);
                           }// for


                           log.debug(" ******************* El contenido del fichero HTML es: " + contenidoHTML.toString());

                           /** Se procede a almacenar el documento html en el gestor con el listado de expedientes pertenecientes a la relaci�n */
                           docGestor.setFichero(contenidoHTML.toString().getBytes());
                           docGestor.setExtension("html");
                           String sNumRelacion = docGestor.getNumeroRelacion().replaceAll("/",GUION);
                           docGestor.setNombreDocumento(ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion);                           
                           docGestor.setNombreFicheroCompleto(ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion);                            
                           docGestor.setTipoMimeContenido(ConstantesDatos.TIPO_MIME_HTML);
                           docGestor.setCodificacionContenido(ConstantesDatos.CODIFICACION_UTF_8);
                          
                           boolean exitoListaRelacion = gestor.setListaExpedientesRelacion(docGestor);
                           if(exitoListaRelacion)
                               log.debug(" ******************* Se guardado la lista de expedientes en la relaci�n");
                           else
                               log.debug(" ******************* No se guardado la lista de expedientes en la relaci�n");
                           
                           exito = true;
                        }
                    }catch(Exception e){
                            exito = false;
                            e.printStackTrace();
                            log.debug(this.getClass().getName() + ".setDocumento: " + e.getMessage());
                            /******************  SI SE PRODUCE UN FALLO AL ALMACENAR EL DOCUMENTO EN EL GESTOR, HAY QUE ELIMINAR LA REFERENCIA AL DOCUMENTO EN EL GESTOR   ******************/
                            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                            tEVO.setCodMunicipio(Integer.toString(docGestor.getCodMunicipio()));
                            tEVO.setCodProcedimiento(docGestor.getCodProcedimiento());
                            tEVO.setEjercicio(Integer.toString(docGestor.getEjercicio()));
                            tEVO.setCodTramite(Integer.toString(docGestor.getCodTramite()));
                            tEVO.setOcurrenciaTramite(Integer.toString(docGestor.getOcurrenciaTramite()));
                            tEVO.setCodDocumento(Integer.toString(docGestor.getCodDocumento()));

                            if(!docGestor.isDocRelacion()){
                                tEVO.setNumeroExpediente(docGestor.getNumeroExpediente());
                                // El documento a eliminar no pertenece a una relaci�n
                                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                                exito = true;
                            }else{
                                // TODO: VERIFICAR CUANDO EL DOCUMENTO PERTENECE A UNA RELACI�N
                                tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                                // El documento a eliminar pertenece a una relaci�n
                                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                                if(resultado>0) exito = true;
                            }
                            /********************************************************/
                            throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitaci�n " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                     }//catch
                }// else



            }
         return exito;
    }






    public byte[] getDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;
        GeneralValueObject gVO = new GeneralValueObject();

        DocumentoGestor docGestor = (DocumentoGestor)doc;
        CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
        docGestor.setImplClassGestor(cache.getImplClassGestor());
        docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
        docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
        docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
        docGestor.setPaswordGestor(cache.getPasswordGestor());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        
      
        try{

            // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
          
           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/
           String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

           if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);
            }// if

           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/        

           docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));
           contenidoDocumento = gestor.getDocumento(docGestor);
           log.debug(" ================>> AlmacenDocumentoTramitacionGestor.getDocumento: " + contenidoDocumento.length);

        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }

        return contenidoDocumento;
    }

    
    
    
    public byte[] getDocumento(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;
        GeneralValueObject gVO = new GeneralValueObject();

        DocumentoGestor docGestor = (DocumentoGestor)doc;
        CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
        docGestor.setImplClassGestor(cache.getImplClassGestor());
        docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
        docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
        docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
        docGestor.setPaswordGestor(cache.getPasswordGestor());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        
        
        try{

            // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
          
           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/
           String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

           if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);
            }// if

           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/        

           docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));
           contenidoDocumento = gestor.getDocumento(docGestor);
           log.debug(" ================>> AlmacenDocumentoTramitacionGestor.getDocumento: " + contenidoDocumento.length);

        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }

        return contenidoDocumento;
    }
    
    
    
    
    public boolean eliminarDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;
        CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
        docGestor.setImplClassGestor(cache.getImplClassGestor());
        docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
        docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
        docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
        docGestor.setPaswordGestor(cache.getPasswordGestor());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        

         try{

            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
            tEVO.setCodMunicipio(Integer.toString(docGestor.getCodMunicipio()));
            tEVO.setCodProcedimiento(docGestor.getCodProcedimiento());
            tEVO.setEjercicio(Integer.toString(docGestor.getEjercicio()));
            tEVO.setCodTramite(Integer.toString(docGestor.getCodTramite()));
            tEVO.setOcurrenciaTramite(Integer.toString(docGestor.getOcurrenciaTramite()));
            tEVO.setCodDocumento(Integer.toString(docGestor.getCodDocumento()));

            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/
           String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

           if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);
            }// if

           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO REEMPLANZANDO LOS CARACTERES ESPECIALES NO PERMITIDOS POR ALFRESCO POR ESPACIOS EN BLANCO*****/
            

            if(!docGestor.isDocRelacion()){
                // EL DOCUMENTO NO PERTENECE A UNA RELACI�N ENTRE EXPEDIENTES                
                Class implClass = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor =(GestorDocumental) implClass.newInstance();                
                docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                if(gestor.eliminarDocumento(docGestor)){                
                    tEVO.setNumeroExpediente(docGestor.getNumeroExpediente());
                    // El documento a eliminar no pertenece a una relaci�n
                    TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                    exito = true;
                }else{
                        throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitaci�n " + docGestor.getNombreDocumento() + " del gestor documental");
                }

            }else{
                  // EL DOCUMENTO NO PERTENECE A UNA RELACI�N ENTRE EXPEDIENTES
                Class implClass = Class.forName(docGestor.getImplClassGestor());                
                docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                // Se borra la referencia al documento de la BBDD pero no del gestor
                //if(gestor.eliminarDocumento(doc)){
                    tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                    int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                    if(resultado>0) exito = true;
                //}else{
                //        throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitaci�n " + docGestor.getNombreDocumento() + " del gestor documental");
                //}
            }

        }catch(TechnicalDocumentoException e){
            // No se ha podido eliminar el documento
            e.printStackTrace();
            log.error(this.getClass().getName() + ".eliminarDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }
         catch(Exception e){
            e.printStackTrace();
           log.error(this.getClass().getName() + ".eliminarDocumento: " + e.getMessage());
           throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitaci�n " + docGestor.getNombreDocumento() + " del gestor documental: " + e.getMessage());
        }

        return exito;
    }
    public boolean isPluginGestor(){
        return true;
    }



    public boolean setDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;

        DocumentoGestor docGestor =  (DocumentoGestor)doc;
        int codMunicipio          = docGestor.getCodMunicipio();
        int ejercicio                = docGestor.getEjercicio();
        String numExpediente = docGestor.getNumeroExpediente();
        byte[] fichero             = docGestor.getFichero();
        String tipoMime          = docGestor.getTipoMimeContenido();
        String nombreDoc      = docGestor.getNombreDocumento();
        String[] params         = docGestor.getParams();
        String extension        = docGestor.getExtension();
        int numeroDocumento = -1;
        
        CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
        docGestor.setImplClassGestor(cache.getImplClassGestor());
        docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
        docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
        docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
        docGestor.setPaswordGestor(cache.getPasswordGestor());
        docGestor.setUrlGestor(cache.getUrlGestor());
        docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        

        try{
            Class implClass = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor =(GestorDocumental) implClass.newInstance();            
           
            ExpedienteOtroDocumentoVO docExterno = new ExpedienteOtroDocumentoVO();
            docExterno.setMunicipio(Integer.toString(codMunicipio));
            docExterno.setEjercicio(Integer.toString(ejercicio));
            docExterno.setNumeroExpediente(numExpediente);
            docExterno.setContenidoDocumento(null);
            docExterno.setTipoDocumento(tipoMime);
            docExterno.setNombreDocumento(nombreDoc);
            docExterno.setExtension(extension);

            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());
            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

            numeroDocumento = ExpedienteOtroDocumentoManager.getInstance().altaDocumento(docExterno, params);
            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));            
            docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

            if(numeroDocumento!=-1){
                // Se ha dado de alta en la bd la referencia al documento externo
                gestor.setDocumento(docGestor);                
                exito = true;
            }else throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental");
            
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoExterno: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoExterno: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental: "  + e.getMessage());
        } catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoExterno: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental: "  + e.getMessage());
        } catch(TechnicalDocumentoException e){
            // No se ha podido dar de alta el documento externo en el gestor => Hay que eliminar la referencia al documento en base de datos
            e.printStackTrace();
            ExpedienteOtroDocumentoManager.getInstance().eliminaDocumento(Integer.toString(codMunicipio),Integer.toString(ejercicio), numExpediente, Integer.toString(numeroDocumento), params);
            throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental: "  + e.getMessage());
        }
        return exito;
    }
    
    public boolean setDocumentoExternoFicheroCSV(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Documento getDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        try{

            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());
            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/


            // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
           docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));
           
           byte[] contenidoDocumento = gestor.getDocumento(docGestor);


           log.debug(this.getClass().getName() + " ******* getDocumentoExterno len contenido :: " + contenidoDocumento.length);
           log.debug(this.getClass().getName() + " ******* getDocumentoExterno ::  " + docGestor.getTipoMimeContenido());
           docGestor.setFichero(contenidoDocumento);


        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }

        return docGestor;

    }

    public boolean eliminarDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        try{
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
                
            int codMunicipio          = doc.getCodMunicipio();
            int ejercicio                = doc.getEjercicio();
            String numExpediente = doc.getNumeroExpediente();
            String numeroDocumento = doc.getNumeroDocumento();
            String[] params               = doc.getParams();

            // Si el documento no pertenece a una relaci�n de expedientes
            Class implClass = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
           

           /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

               
           docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));
           if(gestor.eliminarDocumento(docGestor)){
               ExpedienteOtroDocumentoVO docExterno = new ExpedienteOtroDocumentoVO();
               docExterno.setCodigoDocumento(numeroDocumento);
               docExterno.setMunicipio(Integer.toString(codMunicipio));
               docExterno.setEjercicio(Integer.toString(ejercicio));
               docExterno.setNumeroExpediente(numExpediente);

                if(ExpedienteOtroDocumentoManager.getInstance().eliminaDocumento(Integer.toString(codMunicipio), Integer.toString(ejercicio), numExpediente, numeroDocumento, params))
                    exito = true;
           }else
               throw new AlmacenDocumentoTramitacionException(9,"No se puede eliminar el documento del gestor documental");
           

        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(9,"No se puede eliminar el documento del gestor documental");
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(9,"No se puede eliminar el documento del gestor documental");
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(9,"No se puede eliminar el documento del gestor documental");
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(9,"No se puede eliminar el documento del gestor documental");
        }

        return exito;
    }



   /**
     *  Alta de un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a almacenar
     * @return: Un boolean
     */
    public boolean setDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoGestor docGestor =  (DocumentoGestor)doc;
        try{
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
        
            byte[] contenido = doc.getFichero();
            Documento docBBDD = doc;
            docBBDD.setFichero(null);
            boolean continuar = false;
            if(!doc.isModificarAdjuntoDocExpediente()) // Se da de alta el adjunto asociado al expediente pero sin el contenido
                continuar = ExpedienteDocPresentadoManager.getInstance().setDocumentoPresentado(docBBDD);
            else // Si se modifica el contenido del adjunto  se
                continuar = ExpedienteDocPresentadoManager.getInstance().modificarDocumentoPresentado(docBBDD);


            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/



            if(continuar){
                // Si se ha podido guardar el correspondiente registro en base de datos, se procede a dar de alta el contenido o bien a modificarlo
                // en el gestor documenta
                docGestor.setFichero(contenido);
                Class implClass = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor =(GestorDocumental) implClass.newInstance();

                if(!doc.isModificarAdjuntoDocExpediente())
                   gestor.setDocumento(docGestor);
                else
                    gestor.modificarDocumento(docGestor);
            }  
         
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoPresentado: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(12,"Error al grabar el adjunto asociado a un documento de expediente en el gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoPresentado: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(12,"Error al grabar el adjunto asociado a un documento de expediente en el gestor documental: "  + e.getMessage());
        } catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".setDocumentoPresentado: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(12,"Error al grabar el adjunto asociado a un documento de expediente en el gestor documental: "  + e.getMessage());
        } catch(TechnicalDocumentoException e){
            // No se ha podido dar de alta el documento externo en el gestor => Hay que eliminar la referencia al documento en base de datos
            e.printStackTrace();
            if(!docGestor.isModificarAdjuntoDocExpediente()){ // Se da de alta el adjunto asociado al expediente pero sin el contenido
                ExpedienteDocPresentadoManager.getInstance().eliminarDocumentoPresentado(docGestor);
            }
            throw new AlmacenDocumentoTramitacionException(12,"Error al grabar el adjunto asociado a un documento de expediente en el gestor documental: "  + e.getMessage());
        }

        return exito;
    }

    /**
     *  Recupera el contenido de un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a recuperar
     * @return: Documento
     */
    public Documento getDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        Documento salida = null;
        DocumentoGestor docGestor = (DocumentoGestor)doc;
        GestorDocumental gestor      = null;
        try{
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            Class implClass = Class.forName(docGestor.getImplClassGestor());
            gestor =(GestorDocumental) implClass.newInstance();

            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

            byte[] contenido = gestor.getDocumento(docGestor);
            docGestor.setFichero(contenido);

        }catch(TechnicalDocumentoException e){
            // No se ha podido recuperar el documento del gestor
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(11, "No se ha podido recuperar el contenido del adjunto asociado al documento de expediente del gestor documental");
        }catch(Exception e){
            // No se ha podido recuperar el documento del gestor
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(11, "No se ha podido recuperar el contenido del adjunto asociado al documento de expediente del gestor documental");
        }
        
        return docGestor;
    }

    /**
     * Elimina un documento de expediente que ha sido presentado
     * @param doc: Objeto con los datos necesarios para determinar el documento a eliminar
     * @return: Un boolean
     */
    public boolean eliminarDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;

        DocumentoGestor docGestor = (DocumentoGestor)doc;
        boolean eliminadoDocGestor = false;
        GestorDocumental gestor = null;
        try{            
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            Class implClass = Class.forName(docGestor.getImplClassGestor());
            gestor =(GestorDocumental) implClass.newInstance();

            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                ArrayList<String> carpetas = docGestor.getListaCarpetas();

                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            
            if(gestor.existeDocumento(doc)) // Puede que no existe documento adjunto al expediente y que el documento si figure chequeado como presentado
                eliminadoDocGestor = gestor.eliminarDocumento(docGestor);
            else
                eliminadoDocGestor = true;

            if(eliminadoDocGestor){
                // Si se ha eliminado el documento del gestor documental => Se elimina la referencia al mismo de la base de datos
                exito = ExpedienteDocPresentadoManager.getInstance().eliminarDocumentoPresentado(docGestor);
            }        
        }catch(TechnicalDocumentoException e){
            // No se ha podido eliminar el documento del gestor
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(10, "No se ha podido eliminar el adjunto asociado al documento de expediente del gestor documental");
        }catch(Exception e){
            // No se ha podido instanciar la clase
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(10, "No se ha podido eliminar el adjunto asociado al documento de expediente del gestor documental");
        }

        return exito;
        
    }


    // Recupera el contenido de un documento ajeno al sistema enviado al portafirmas para que sea firmado
    public DocumentoOtroFirmaVO getDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{
        DocumentoOtroFirmaVO salida = new DocumentoOtroFirmaVO();

        log.debug(this.getClass().getName() + ".getDocumentoExternoPortafirmas() ================>");
        salida = EDocExtPortafirmasManager.getInstance().getInfoDocumento(Integer.toString(doc.getCodigoDocumento()),doc.getParams());
        if(salida!=null){
            // Se recupera el contenido del documento de Alfresco
            String nifUsuario               = salida.getNifUsuario();
            String nombreDocumento         = salida.getNombreDocumento();
            String extension               = salida.getExtension();
            int codigoDocumento        = salida.getCodigoDocumento();
            
            log.debug("getDocumentoExternoPortafirmas nifUsuario: " + nifUsuario);
            log.debug("getDocumentoExternoPortafirmas nombreDocumento: " + nombreDocumento);
            log.debug("getDocumentoExternoPortafirmas extension: " + extension);
            log.debug("getDocumentoExternoPortafirmas codigoDocumento: " + codigoDocumento);

            ResourceBundle bundle = ResourceBundle.getBundle("documentos");            
            String nombreGestor = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA + ConstantesDatos.GESTOR + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR );
            String carpeta_1 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_CARPETA_RAIZ);
            String carpeta_2 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_PORTAFIRMAS);
            String carpeta_3 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_DOCUMENTOS);

            String urlAlfresco = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_URL);
            String usuario     = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor +  ConstantesDatos.SUFIJO_PLUGIN_GESTOR_USUARIO_GESTOR);
            String password  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_PASSWORD_GESTOR);

            log.debug(" carpeta_1: " + carpeta_1 + ", carpeta_2: " + carpeta_2 + ", carpeta_3: " + carpeta_3);
            DocumentoGestor docGestor = new DocumentoGestor();
            ArrayList<String> carpetas = new ArrayList<String>();
            carpetas.add(carpeta_1); carpetas.add(carpeta_2); carpetas.add(carpeta_3);carpetas.add(nifUsuario);
            docGestor.setUrlGestor(urlAlfresco);
            docGestor.setUsuarioGestor(usuario);
            docGestor.setPaswordGestor(password);
          

            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
            String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());
            if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){
                
                for(int i=0;carpetas!=null && i<carpetas.size();i++){
                    String carpeta = carpetas.get(i);
                    log.debug("carpeta: " + carpeta);
                    carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                    carpetas.set(i, carpeta);
                }// for
                docGestor.setListaCarpetas(carpetas);

            }// if
            /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

            docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(codigoDocumento + ConstantesDatos.GUION_BAJO + nombreDocumento + ConstantesDatos.DOT + extension,listaCaracteresNoPermitidos));

            GestorAlfrescoImpl gestor = new GestorAlfrescoImpl();
            byte[] contenido = null;
            try{
                contenido = gestor.getDocumento(docGestor);
                salida.setContenido(contenido);
            }catch(TechnicalDocumentoException e){
                e.printStackTrace();
                salida = new DocumentoOtroFirmaVO();
            }
        }//if

        return salida;
    }

    // Da de alta un documento ajeno al sistema para que pueda ser firmado en el portafirmas
    public boolean setFirmaDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoOtroFirmaVO salida = new DocumentoOtroFirmaVO();
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(doc.getParams());
        Connection connection = null;
        log.debug(this.getClass().getName() + ".setFirmaDocumentoExternoPortafirmas() ================>");
        
        try{
            
            connection = adapt.getConnection();
            adapt.inicioTransaccion(connection);

            // Se almacena la firma en base de datos
            boolean firmaAlmacenadaBD = EDocExtPortafirmasDAO.getInstance().guardarFirma(doc, connection);
            if(firmaAlmacenadaBD){
                // Si se ha almacenada correctamente la firma en base de datos => Se procede
                salida = EDocExtPortafirmasDAO.getInstance().getInfoDocumento(Integer.toString(doc.getCodigoDocumento()),connection);

                // Se recupera el contenido del documento de Alfresco
                String nifUsuario               = salida.getNifUsuario();
                String nombreDocumento  = salida.getNombreDocumento();
                String extension               = salida.getExtension();
                int codigoDocumento        = salida.getCodigoDocumento();

                log.debug("getDocumentoExternoPortafirmas nifUsuario: " + nifUsuario);
                log.debug("getDocumentoExternoPortafirmas nombreDocumento: " + nombreDocumento);
                log.debug("getDocumentoExternoPortafirmas extension: " + extension);
                log.debug("getDocumentoExternoPortafirmas codigoDocumento: " + codigoDocumento);

                ResourceBundle bundle = ResourceBundle.getBundle("documentos");
                String nombreGestor = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA + ConstantesDatos.GESTOR + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR );
                String carpeta_1 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_CARPETA_RAIZ);
                String carpeta_2 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_PORTAFIRMAS);
                String carpeta_3 = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.BARRA + ConstantesDatos.PROP_GESTOR_DOC_EXT_PORTAFIRMAS_DOCUMENTOS);

                String urlAlfresco = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_URL);
                String usuario     = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor +  ConstantesDatos.SUFIJO_PLUGIN_GESTOR_USUARIO_GESTOR);
                String password  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + doc.getCodOrganizacion() + ConstantesDatos.BARRA +  nombreGestor +  ConstantesDatos.SUFIJO_PLUGIN_GESTOR_PASSWORD_GESTOR);

                log.debug(" carpeta_1: " + carpeta_1 + ", carpeta_2: " + carpeta_2 + ", carpeta_3: " + carpeta_3);
                DocumentoGestor docGestor = new DocumentoGestor();
                ArrayList<String> carpetas = new ArrayList<String>();
                carpetas.add(carpeta_1); carpetas.add(carpeta_2); carpetas.add(carpeta_3);carpetas.add(nifUsuario);

                /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/
                String[] listaCaracteresNoPermitidos = getListaCaracteresNoPermitidos(docGestor.getCodMunicipio());

                if(listaCaracteresNoPermitidos!=null && listaCaracteresNoPermitidos.length>0){

                    for(int i=0;carpetas!=null && i<carpetas.size();i++){
                        String carpeta = carpetas.get(i);
                        log.debug("carpeta: " + carpeta);
                        carpeta = sustituirCaracteresEspeciales(carpeta,listaCaracteresNoPermitidos);
                        carpetas.set(i, carpeta);
                    }// for
                    docGestor.setListaCarpetas(carpetas);

                }// if
                /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

                /** original
                 * docGestor.setListaCarpetas(carpetas);
                 */
                docGestor.setUrlGestor(urlAlfresco);
                docGestor.setUsuarioGestor(usuario);
                docGestor.setPaswordGestor(password);
                /** original
                docGestor.setNombreFicheroCompleto(codigoDocumento + ConstantesDatos.GUION_BAJO + nombreDocumento + ConstantesDatos.DOT + extension + ConstantesDatos.DOT + ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
                 **/
                docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(codigoDocumento + ConstantesDatos.GUION_BAJO + nombreDocumento + ConstantesDatos.DOT + extension + ConstantesDatos.DOT + ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION,listaCaracteresNoPermitidos));


                docGestor.setFichero(doc.getFirma());
                GestorAlfrescoImpl gestor = new GestorAlfrescoImpl();                
                try{
                    String url = gestor.setDocumento(docGestor);
                    if(url!=null && url.length()>0){
                        adapt.finTransaccion(connection);
                        exito = true;
                    }

                }catch(TechnicalDocumentoException e){
                    // Si no se ha podido guardar el documento en Alfresco se deshace la transacci�n
                    e.printStackTrace();
                    exito = false;
                    try{
                        // Se aborta la transacci�n
                        adapt.rollBack(connection);
                    }catch(BDException f){
                        f.printStackTrace();
                    }
                }

            } //if
            else {
                // No se ha podido guardar la firma en base de datos
                exito = false;
                try{
                    // Se aborta la transacci�n
                    adapt.rollBack(connection);
                    
                }catch(BDException f){
                    f.printStackTrace();
                } 
            }
        }
        catch(Exception e){
            e.printStackTrace();
            exito = false;            
            try{
                // Se aborta la transacci�n
                adapt.rollBack(connection);
            }catch(BDException f){
                f.printStackTrace();
            }
            
        }

        return exito;
    }

    //public boolean setFirmaDocumentoExpediente(DocumentoProcedimientoFirmaVO doc) throws AlmacenDocumentoTramitacionException {
    public boolean setFirmaDocumentoExpediente(Documento doc) throws AlmacenDocumentoTramitacionException {     
        boolean exito = false;
        DocumentoGestor docGestor = null;        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{                                    
            docGestor = (DocumentoGestor)doc;
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            adapt = new AdaptadorSQLBD(docGestor.getParams());
            con = adapt.getConnection();
            
            adapt.inicioTransaccion(con);
            DocumentoProcedimientoFirmaVO documento = new DocumentoProcedimientoFirmaVO();
            documento.setFirma(docGestor.getFichero());
            documento.setIdPresentado(docGestor.getCodDocumento());
            documento.setCodigoUsuarioFirma(Integer.toString(docGestor.getCodUsuario()));
            documento.setFechaFirma(Calendar.getInstance());
            documento.setCodOrganizacion(Integer.toString(docGestor.getCodMunicipio()));        
            documento.setCodigoDocumento(Integer.parseInt(docGestor.getNumeroDocumento()));
            documento.setParams(docGestor.getParams());        
            documento.setObservaciones(docGestor.getObservaciones());                
            documento.setIdNumFirma(docGestor.getIdFirma());
                        
            if(DocsProcedimientoPortafirmasDAO.getInstance().guardarFirma(documento, con)){            
                Class implClass = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor =(GestorDocumental) implClass.newInstance();                
                docGestor.setNombreFicheroCompleto(docGestor.getNumeroDocumento() + "-" + docGestor.getNombreDocumento() + "." + docGestor.getExtension());                
                gestor.setDocumento(docGestor);
                exito = true;
            }
            
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener conexi�n  a la BBDD: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de expediente en Alfresco: " + e.getMessage());           
        }catch(TechnicalException e){
            e.printStackTrace();
            log.error("Error al grabar los datos de la firma en BBDD: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de expediente en Alfresco: " + e.getMessage());           
        }catch(ClassNotFoundException e){                
            e.printStackTrace();
            log.error("Error al instanciar la clase fachada de comunicaci�n con Alfresco: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de inicio de expediente en Alfresco: " + e.getMessage());
        }catch(InstantiationException e){                
            e.printStackTrace();
            log.error("Error al instanciar la clase fachada de comunicaci�n con Alfresco: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de inicio de expediente en Alfresco: " + e.getMessage());
        }catch(IllegalAccessException e){                
            e.printStackTrace();
            log.error("Error al instanciar la clase fachada de comunicaci�n con Alfresco: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de inicio de expediente en Alfresco: " + e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento de inicio de expediente en Alfresco: " + e.getMessage());
        }finally{
            
            try{
                if(exito) 
                    adapt.finTransaccion(con);
                else
                    adapt.rollBack(con);
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }     
        return exito;
    }
        
        
     public boolean setFirmaDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{  
        boolean exito = false;        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DocumentoGestor docGestor = (DocumentoGestor)doc;
        boolean continuar = false;
        try{
            
            ResourceBundle config = ResourceBundle.getBundle("Portafirmas");
            adapt = new AdaptadorSQLBD(docGestor.getParams());
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();            
            adjunto.setFirma(new String(docGestor.getFichero()));            
            adjunto.setEstadoFirma(ConstantesDatos.ESTADO_FIRMA_FIRMADO);            
            String plataformaFirma = config.getString(docGestor.getCodMunicipio()  + "/PluginPortafirmas");
            adjunto.setPlataformaFirma(plataformaFirma);
            adjunto.setCodUsuarioFirmaOtro(docGestor.getCodUsuario());
            adjunto.setTipoCertificadoFirma(ConstantesDatos.TIPO_CERTIFICADO_USUARIO);
            adjunto.setIdDocExterno(Integer.parseInt(docGestor.getNumeroDocumento()));
            adjunto.setCodigoNotificacion(docGestor.getCodDocumento());
            adjunto.setFechaFirma(Calendar.getInstance());
            exito = AdjuntoNotificacionManager.getInstance().setFirmaAdjuntoExternoNotificacion(adjunto,doc.getParams());        
            continuar = true;
            
            if(continuar){                       
                CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
                docGestor.setUrlGestor(cache.getUrlGestor());
                docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
                docGestor.setImplClassGestor(cache.getImplClassGestor());
                docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
                docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
                docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
                docGestor.setPaswordGestor(cache.getPasswordGestor());
                docGestor.setUrlGestor(cache.getUrlGestor());
                docGestor.setUsuarioGestor(cache.getUsuarioGestor());
                
                // Se procede a dar de alta la firma en Alfresco
                docGestor.setNombreFicheroCompleto(docGestor.getNumeroDocumento() + "-" + docGestor.getCodTramite() + "-" + docGestor.getOcurrenciaTramite() + "-" + docGestor.getNombreDocumento() + ConstantesDatos.DOT + docGestor.getExtension());
                Class implClass           = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
                gestor.setDocumento(docGestor);
                exito = true;                                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al almacenar la firma de un documento externo de una notificaci�n en Alfresco: " + e.getMessage());                   
        }finally{
            try{
                if(exito){
                    adapt.finTransaccion(con);
                }else
                    adapt.rollBack(con);
                
            }catch(BDException e){
                e.printStackTrace();
            }
        }
       
        return exito;
    }
    
    
    public Documento getDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{

        DocumentoGestor docGestor = (DocumentoGestor)doc;
        
        try{
            
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());            
            
            docGestor.setNombreFicheroCompleto(docGestor.getCodDocumento() + "-" + docGestor.getCodTramite() + "-" + docGestor.getOcurrenciaTramite() + "-" + docGestor.getNombreDocumento() + ConstantesDatos.DOT + docGestor.getExtension());
            Class implClass           = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
            byte[] contenido = gestor.getDocumento(docGestor);
            docGestor.setFichero(contenido);
            
        }catch(Exception e){
            throw new AlmacenDocumentoTramitacionException(2,"Error al recuperar un documento externo de una notificaci�n electr�nica de Alfresco: " + e.getMessage());
        }
        
        return docGestor;
        
    }

    
    public boolean setDocumentoDatoSuplementarioTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito;
        DocumentoGestor docGestor = null; 
        byte[] contenido = null;
        String nombreDocumentoAnterior = null;
        
        try{            
            docGestor = (DocumentoGestor)doc;
            contenido = docGestor.getFichero();
            docGestor.setFichero(null);
            
            // En primer lugar se comprueba si en el campo suplementario ya hab�a anteriormente un fichero.
            // En ese caso, se recupera su nombre, ya que es necesario compararlo con el nuevo para eliminar
            // en alfresco el viejo y dar de alta el nuevo
                        
            nombreDocumentoAnterior = DatosSuplementariosDAO.getInstance().getNombreCampoSuplementarioFicheroTramite(doc.getCodMunicipio(),doc.getCodTramite(),doc.getOcurrenciaTramite(),doc.getEjercicio(),doc.getNumeroExpediente(),doc.getCodTipoDato(),con);
            log.debug("nombreDocumentoAnterior: " + nombreDocumentoAnterior);                       
            
            // Se graba el registro correspondiente en la tabla E_TFI con al informaci�n del documento pero 
            // sin el contenido, que se enviar� al gestor documental pertinente
            exito = DatosSuplementariosDAO.getInstance().grabarDocumentoDatoSuplementarioTramite(docGestor,con);

         }catch(TechnicalException e){
            log.error("setDocumentoDatoSuplementarioTramite() - Error al dar de alta un documento  en un campo suplementario fichero de expediente: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,"Error al dar de alta un documento en un campo suplementario fichero de expediente: " + e.getMessage());  
         }           


         try{                
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor()); 
             
            Class implClass           = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
            docGestor.setFichero(contenido);
            String nombreFicheroNuevo = docGestor.getCodTipoDato() + "_" + docGestor.getCodTramite() + "_" + docGestor.getOcurrenciaTramite() + "_" + docGestor.getNombreDocumento();
            docGestor.setNombreFicheroCompleto(nombreFicheroNuevo);
            
            if(nombreDocumentoAnterior!=null && !"".equals(nombreDocumentoAnterior)){
                // Anteriormente el campo ya alojaba un fichero
                docGestor.setNombreFicheroCompleto(docGestor.getCodTipoDato() + "_" + docGestor.getCodTramite() + "_" + docGestor.getOcurrenciaTramite() + "_" + nombreDocumentoAnterior);
                docGestor.setNuevoNombreFicheroCompleto(nombreFicheroNuevo);
                gestor.modificarDocumento(docGestor);
            }else
                gestor.setDocumento(docGestor);     
            
            exito = true;
             
         }catch(TechnicalDocumentoException e){             
             log.error("Error al almacenar un documento en Alfresco: " + e.getDescripcionError());
             throw new AlmacenDocumentoTramitacionException(1,"Error al dar de alta un documento de registro en Alfresco: " + e.getMessage());             
         }catch(ClassNotFoundException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(InstantiationException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(IllegalAccessException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }         
        return exito;    
   }
        
     
     public void setDocumentosRegistro(ArrayList<Documento> documentos,Connection con) throws AlmacenDocumentoTramitacionException{
     
         AnotacionRegistroDAO dao = AnotacionRegistroDAO.getInstance();
         try{
             dao.insertarDocsRegistroSinBinario(con,documentos);
         }catch(AnotacionRegistroException e){
             // Si ocurre un error, se lanza la excepci�n para realizar un rollback
             throw new AlmacenDocumentoTramitacionException(0,"Error al dar de alta la informaci�n de los documentos de registro en base de datos: " + e.getMessage());             
         }
         
         
         // Si se ha insertado correctamente los registros correspondientes a cada documento
         // en la tabla R_RED, llega el momento de enviar el contenido de los documentos a alfresco.
         
         
         try{
             for(int i=0;documentos!=null && i<documentos.size();i++){            
                DocumentoGestor docGestor = (DocumentoGestor)documentos.get(i);
                
                // Sino se trata de un documento de la lista de documentos de inicio del procedimiento asociado, y que no tiene valor
                if(!docGestor.esDocumentoProcedimientoSinValor() && docGestor.getFichero()!=null && docGestor.getFichero().length>0){
                    CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(docGestor.getCodMunicipio(),getNombreServicio());
                    docGestor.setUrlGestor(cache.getUrlGestor());
                    docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
                    docGestor.setImplClassGestor(cache.getImplClassGestor());
                    docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
                    docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
                    docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
                    docGestor.setPaswordGestor(cache.getPasswordGestor());
                    docGestor.setUrlGestor(cache.getUrlGestor());
                    docGestor.setUsuarioGestor(cache.getUsuarioGestor()); 
                    Class implClass           = Class.forName(docGestor.getImplClassGestor());
                    GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
                    docGestor.setNombreFicheroCompleto(docGestor.getNombreDocumento() + "." + docGestor.getExtension());

                    gestor.setDocumento(docGestor);                    
                }
                
             }// for
             
         }catch(TechnicalDocumentoException e){             
             log.error("Error al almacenar un documento en Alfresco: " + e.getDescripcionError());
             throw new AlmacenDocumentoTramitacionException(1,"Error al dar de alta un documento de registro en Alfresco: " + e.getMessage());             
         }catch(ClassNotFoundException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(InstantiationException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(IllegalAccessException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }
     }
     
     
     /**
      * Recupera un documento determinado asociado a una anotaci�n de registro
      * @param doc: Objeto de tipo DocumentoGestor
      * @return DocumentoGestor
      * @throws AlmacenDocumentoTramitacionException si ocurre alg�n error
      */
     public Documento getDocumentoRegistro(Documento doc) throws AlmacenDocumentoTramitacionException{
                  
         try{
              DocumentoGestor docGestor = (DocumentoGestor)doc;
              CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
              docGestor.setUrlGestor(cache.getUrlGestor());
              docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
              docGestor.setImplClassGestor(cache.getImplClassGestor());
              docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
              docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
              docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
              docGestor.setPaswordGestor(cache.getPasswordGestor());
              docGestor.setUrlGestor(cache.getUrlGestor());
              docGestor.setUsuarioGestor(cache.getUsuarioGestor());           
              
              Class implClass           = Class.forName(docGestor.getImplClassGestor());
              GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
              docGestor.setNombreFicheroCompleto(docGestor.getNombreDocumento() + "." + docGestor.getExtension());              
              byte[] fichero  = gestor.getDocumento(docGestor);     
              doc.setFichero(fichero);
              
         }catch(Exception e){
             log.error("Se ha producido un error al recuperar : " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(3,"Error al recuperar el contenido de un documento de registro alojado en Alfresco: " + e.getMessage());
         }         
         return doc;
     }
     
    // Recupera un documento de los datos suplementarios de tipo fichero de expediente
    public Documento getDocumentoDatosSuplementarios(Documento doc) throws AlmacenDocumentoTramitacionException{
         
       DocumentoGestor docGestor = null;
       byte[] contenidoDocumento = null;
       
       try{

           docGestor = (DocumentoGestor) doc;           
           CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
           docGestor.setImplClassGestor(cache.getImplClassGestor());
           docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
           docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
           docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
           docGestor.setPaswordGestor(cache.getPasswordGestor());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setUsuarioGestor(cache.getUsuarioGestor());            
           
            // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
           contenidoDocumento = gestor.getDocumento(docGestor);
           log.debug(" ================>> AlmacenDocumentoTramitacionGestor.getDocumento: " + contenidoDocumento.length);
           docGestor.setFichero(contenidoDocumento);
           
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }

        return docGestor;               
    }

    // Recupera un documento de los datos suplementarios del tipo fichero de tramite
    public Documento getDocumentoDatosSuplementariosTramite(Documento doc) throws AlmacenDocumentoTramitacionException{
     
       DocumentoGestor docGestor = null;
       byte[] contenidoDocumento = null;
       
       try{
           docGestor = (DocumentoGestor) doc;
           
           CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
           docGestor.setImplClassGestor(cache.getImplClassGestor());
           docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
           docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
           docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
           docGestor.setPaswordGestor(cache.getPasswordGestor());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setUsuarioGestor(cache.getUsuarioGestor());
                      
            // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();           
           contenidoDocumento = gestor.getDocumento(docGestor);
           log.debug(" ================>> AlmacenDocumentoTramitacionGestor.getDocumento: " + contenidoDocumento.length);
           docGestor.setFichero(contenidoDocumento);
           
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al recuperar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(e.getCodigo(),e.getDescripcionError());
        }

        return docGestor;              
        
    }
 
    // Elimina un documento de los datos suplementarios de expediente
    public boolean eliminarDocumentoDatosSuplementarios (Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;        
        DocumentoGestor docGestor = null;
        
        try{            
            docGestor = (DocumentoGestor)doc;
            // Se elimina el registro asociado al documento de la base de datos
            exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementarios(docGestor, con);
                
        }catch(TechnicalException e){
            exito = false;
            log.error("Error al eliminar el archivo de gestor documental Alfresco: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el archivo de gestor documental Alfresco: " + e.getMessage());
        }
        
        
        try{

           exito = false;
           
           CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
           docGestor.setImplClassGestor(cache.getImplClassGestor());
           docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
           docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
           docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
           docGestor.setPaswordGestor(cache.getPasswordGestor());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setUsuarioGestor(cache.getUsuarioGestor());
           
           // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();                      
           gestor.eliminarDocumento(docGestor);
           exito = true;
           
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getDescripcionError());
        }

        return exito;
    }
    
    // Elimina un documentos de los datos suplementarios de tramite
    public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
     boolean exito = false;        
        DocumentoGestor docGestor = null;
        
        try{            
            docGestor = (DocumentoGestor)doc;
            // Se elimina el registro asociado al documento del campo suplementario de tr�mite de la base de datos            
            exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementariosTramite(doc, con);
                
        }catch(TechnicalException e){
            exito = false;
            log.error("Error al eliminar el archivo de gestor documental Alfresco: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el archivo de gestor documental Alfresco: " + e.getMessage());
        }
        
        
        try{

           exito = false;
           CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
           docGestor.setImplClassGestor(cache.getImplClassGestor());
           docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
           docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
           docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
           docGestor.setPaswordGestor(cache.getPasswordGestor());
           docGestor.setUrlGestor(cache.getUrlGestor());
           docGestor.setUsuarioGestor(cache.getUsuarioGestor());
           
           // Si el documento no pertenece a una relaci�n de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                     
           gestor.eliminarDocumento(docGestor);
           exito = true;
           
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getDescripcionError());
        }

        return exito;
    }
         
     /**
      * Elimina un documento asociado a una anotaci�n de registro del gestor documental Alfresco
      * @param doc: Objeto de tipo DocumentoGestor que contiene la informaci�n necesaria del documento para poder eliminarlo
      * @param con: Conexi�n a la BBDD
      * @throws AlmacenDocumentoTramitacionException: Si ocurre alg�n error
      */
     public boolean eliminarDocumentoRegistro(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
         boolean exito = false;
         
         try{                   
            AnotacionRegistroDAO.getInstance().eliminarDocumentoRegistro(doc,con);
            exito = true;         
        }catch(AnotacionRegistroException e){
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar un documento asociado a una anotaci�n de registro: " + e.getMessage());
        }
         
         // Si se ha eliminado el registro de base de datos de la tabla R_RED, se procede a eliminar el documento
         // de Alfresco
         try{
             
                  
              DocumentoGestor docGestor = (DocumentoGestor)doc;
              
              
              log.debug(" ===================> fechaDocumento: " + docGestor.getFechaDocumento());
              log.debug(" ===================> extension: " + docGestor.getExtension());
              log.debug(" ===================> nombreDocumento: " + docGestor.getNombreDocumento());
              log.debug(" ===================> nombreDocumento: " + docGestor.getNombreDocumento());
              
              if(docGestor.getFechaDocumento()!=null && docGestor.getTipoMimeContenido()!=null && !docGestor.getFechaDocumento().equals("") && !docGestor.getTipoMimeContenido().equals("")){
                    CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
                    docGestor.setNombreFicheroCompleto(docGestor.getNombreDocumento() + "." + docGestor.getExtension());              
                    docGestor.setUrlGestor(cache.getUrlGestor());
                    docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
                    docGestor.setImplClassGestor(cache.getImplClassGestor());
                    docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
                    docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
                    docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
                    docGestor.setPaswordGestor(cache.getPasswordGestor());
                    docGestor.setUrlGestor(cache.getUrlGestor());
                    docGestor.setUsuarioGestor(cache.getUsuarioGestor());

                    Class implClass           = Class.forName(docGestor.getImplClassGestor());
                    GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
                    exito = gestor.eliminarDocumento(docGestor);     
              }              
         }catch(Exception e){
             log.error("Se ha producido un error al eliminar el documento " + doc.getNombreDocumento() + " asociado a la anotaci�n de registro " + doc.getEjercicioAnotacion() + "-" + doc.getNumeroRegistro() + " de tipo " + doc.getTipoRegistro() + " : " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(4,"Se ha producido un error al eliminar el documento " + doc.getNombreDocumento() + " asociado a la anotaci�n de registro " + doc.getEjercicioAnotacion() + "-" + doc.getNumeroRegistro() + " de tipo " + doc.getTipoRegistro() + " : " + e.getMessage());
         }         
         
         return exito;
     }
     
     
     
     public boolean setDocumentoDatoSuplementarioExpediente(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito;
        DocumentoGestor docGestor = null; 
        byte[] contenido = null;
        String nombreDocumentoAnterior = null;
        
        try{            
            docGestor = (DocumentoGestor)doc;
            contenido = docGestor.getFichero();
            docGestor.setFichero(null);
            
            // En primer lugar se comprueba si en el campo suplementario ya hab�a anteriormente un fichero.
            // En ese caso, se recupera su nombre, ya que es necesario compararlo con el nuevo para eliminar
            // en alfresco el viejo y dar de alta el nuevo
                        
            nombreDocumentoAnterior = DatosSuplementariosDAO.getInstance().getNombreCampoSuplementarioFicheroExpediente(Integer.toString(docGestor.getCodMunicipio()), Integer.toString(docGestor.getEjercicio()),docGestor.getNumeroExpediente(),docGestor.getCodTipoDato(), con);
            log.debug("nombreDocumentoAnterior: " + nombreDocumentoAnterior);
                       
            
            // Se graba el registro correspondiente en la tabla E_TFI con al informaci�n del documento pero 
            // sin el contenido, que se enviar� al gestor documental pertinente
            exito = DatosSuplementariosDAO.getInstance().grabarDocumentoDatosSuplementarioFicheroExpediente(docGestor,con);

         }catch(TechnicalException e){
            log.error("setDocumentoDatoSuplementarioExpediente() - Error al dar de alta un documento  en un campo suplementario fichero de expediente: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(0,"Error al dar de alta un documento en un campo suplementario fichero de expediente: " + e.getMessage());  
         }           


         try{
           
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
             
             
            Class implClass           = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();                
            docGestor.setFichero(contenido);
            String nombreFicheroNuevo = docGestor.getCodTipoDato() + "_" + docGestor.getNombreDocumento();
            docGestor.setNombreFicheroCompleto(nombreFicheroNuevo);
                                    
            if(nombreDocumentoAnterior!=null && !"".equals(nombreDocumentoAnterior)){
                // Anteriormente el campo ya alojaba un fichero
                docGestor.setNombreFicheroCompleto(docGestor.getCodTipoDato() + "_" + nombreDocumentoAnterior);
                docGestor.setNuevoNombreFicheroCompleto(nombreFicheroNuevo);
                gestor.modificarDocumento(docGestor);
            }else
                gestor.setDocumento(docGestor);     
            
            exito = true;
             
         }catch(TechnicalDocumentoException e){             
             log.error("Error al almacenar un documento en Alfresco: " + e.getDescripcionError());
             throw new AlmacenDocumentoTramitacionException(1,"Error al dar de alta un documento de registro en Alfresco: " + e.getMessage());             
         }catch(ClassNotFoundException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(InstantiationException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }catch(IllegalAccessException e){
             log.error("Error al instanciar el cliente de Alfresco: " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(2,"Error al instanciar el cliente de Alfresco: " + e.getMessage());             
         }
         
         
         
         
        return exito;    
         
     }
     
     
     
    public boolean eliminarTodosDocumentosDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        ArrayList<CampoSuplementarioFicheroVO> campos = new ArrayList<CampoSuplementarioFicheroVO>();
        
        try{                   
            // Se recuperan los datos de los campos suplementarios de tipo fichero del tr�mite y expediente para 
            // poder borrar sus datos uno a uno en Alfresco
            campos = DatosSuplementariosDAO.getInstance().getCodigosCamposSuplementarioFicheroTramite(doc, con);
                  
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar todos los archivos almacenados en campos suplementarios de tipo fichero de un determinado tr�mite, debido a que no se han recuperado los c�digos de los campos de BBDD: " + e.getMessage());
        }
        
        
         try{
             
            DocumentoGestor docGestor = (DocumentoGestor)doc;
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
                
             Class implClass = Class.forName(docGestor.getImplClassGestor());
                
             for(int i=0;campos!=null && i<campos.size();i++){
                 
                CampoSuplementarioFicheroVO campo = campos.get(i);
                // Se procede a invocar la operaci�n de eliminarDocumento en Alfresco, para cada campo
                // recuperado
                                
                docGestor.setNombreFicheroCompleto(campo.getCodigoCampo() + "_" + docGestor.getCodTramite() + "_" + docGestor.getOcurrenciaTramite() + "_" + campo.getNombreFichero()); 
                GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                gestor.eliminarDocumento(docGestor);
              
             }
           
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(InstantiationException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(IllegalAccessException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getMessage());
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(4,"Error al eliminar el documento del gestor documental: "  + e.getDescripcionError());
        }
        
        
        
        return exito;
    }
      
 
    public Documento getDocumentoRegistroConsulta(Documento doc,String origen) throws AlmacenDocumentoTramitacionException{
         try{
              DocumentoGestor docGestor = (DocumentoGestor)doc;
              CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
              docGestor.setUrlGestor(cache.getUrlGestor());
              docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
              docGestor.setImplClassGestor(cache.getImplClassGestor());
              docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
              docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
              docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
              docGestor.setPaswordGestor(cache.getPasswordGestor());
              docGestor.setUrlGestor(cache.getUrlGestor());
              docGestor.setUsuarioGestor(cache.getUsuarioGestor());
              docGestor.setNombreFicheroCompleto(docGestor.getNombreDocumento() + "." + docGestor.getExtension());
              
              Class implClass           = Class.forName(docGestor.getImplClassGestor());
              GestorDocumental gestor   =(GestorDocumental) implClass.newInstance();   
              byte[] fichero  = gestor.getDocumento(docGestor);     
              doc.setFichero(fichero);
              
         }catch(Exception e){
             log.error("Se ha producido un error al recuperar : " + e.getMessage());
             throw new AlmacenDocumentoTramitacionException(3,"Error al recuperar el contenido de un documento de registro alojado en Alfresco: " + e.getMessage());
         }         
         return doc;
    }
 
    
     /**
     * Operaci�n que da de alta un documento externo que se adjunta a una notificaci�n electr�nica que se env�a
     * al finalizar un tr�mite de un determinado expediente. El binario se da de alta en Alfresco
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
            
            DocumentoGestor docGestor = (DocumentoGestor)doc;
            params = docGestor.getParams();
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
            adjunto.setCodigoMunicipio(docGestor.getCodMunicipio());
            adjunto.setCodigoTramite(docGestor.getCodTramite());
            adjunto.setOcurrenciaTramite(docGestor.getOcurrenciaTramite());
            adjunto.setNumeroExpediente(docGestor.getNumeroExpediente());            
            adjunto.setNombre(docGestor.getNombreDocumento() + ConstantesDatos.DOT + docGestor.getExtension());
            adjunto.setContentType(docGestor.getTipoMimeContenido());                
            adjunto.setCodigoNotificacion(docGestor.getCodDocumento());
            
            int codigo = AdjuntoNotificacionDAO.getInstance().insertarAdjuntoExternosSinBinario(adjunto, params[0], con);
            if(codigo>0){
                String nombreFicheroCompleto = codigo + "-" + docGestor.getCodTramite() + "-" + docGestor.getOcurrenciaTramite() + "-" + docGestor.getNombreDocumento() + ConstantesDatos.DOT + docGestor.getExtension();
                docGestor.setNombreFicheroCompleto(nombreFicheroCompleto);
                CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
                docGestor.setUrlGestor(cache.getUrlGestor());
                docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
                docGestor.setImplClassGestor(cache.getImplClassGestor());
                docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
                docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
                docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
                docGestor.setPaswordGestor(cache.getPasswordGestor());
                docGestor.setUrlGestor(cache.getUrlGestor());
                docGestor.setUsuarioGestor(cache.getUsuarioGestor());
                
                Class implClass          = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor  =(GestorDocumental) implClass.newInstance();                                
                gestor.setDocumento(docGestor);
                adapt.finTransaccion(con);
                exito =true;
            }
            
        }catch(BDException e){            
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                e.printStackTrace();
            }
            
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al obtener una conexi�n a la BBDD al tratar de almacenar un documento externo asociado a una notificaci�n electr�nica: " + e.getMessage());
        }
        catch(Exception e){            
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                e.printStackTrace();
            }
            
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(1,"Error al obtener una conexi�n a la BBDD al tratar de almacenar un documento externo asociado a una notificaci�n electr�nica: " + e.getMessage());
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
     * Elimina un documento externo asociado a una notificaci�n de la BBDD
     * @param doc: Objeto que implementa la interfaz Documento y que contiene la informaci�n del documento a eliminar
     * @return True siha ido todo bien y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException si ocurre alg�n error
     */
    public boolean eliminarDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        boolean eliminadoAlfresco = false;
        
        try{
            DocumentoGestor docGestor = (DocumentoGestor)doc;
            String nombreFicheroCompleto = docGestor.getCodDocumento() + "-" + docGestor.getCodTramite() + "-" + docGestor.getOcurrenciaTramite() + "-" + docGestor.getNombreDocumento() + ConstantesDatos.DOT + docGestor.getExtension();
            docGestor.setNombreFicheroCompleto(nombreFicheroCompleto);
            
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            Class implClass          = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor  =(GestorDocumental) implClass.newInstance();                 
            gestor.eliminarDocumento(docGestor);
            eliminadoAlfresco = true;
            
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(3,"Error al eliminar un documento externo asociado a una notificaci�n almacenado en Alfresco: " + e.getMessage());
        }        
        
        
        try{
            if(eliminadoAlfresco){
                // Si se ha eliminado de alfresco, se borra de la base de datos
                exito = AdjuntoNotificacionManager.getInstance().eliminarAdjuntoExterno(doc.getCodDocumento(),doc.getParams());
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new AlmacenDocumentoTramitacionException(3,"Error al eliminar un documento externo asociado a una notificaci�n almacenado en Alfresco: " + e.getMessage());
        }
        
        return exito;
    }
    
    
    
    /**
     * Permite recuperar la firma de un documento externo asociado a una notificaci�n
     * @param doc: Objeto que implementa la interfaz Documento con los datos del documento a recuperar
     * @param con: Conexi�n a la BBDD
     * @return String con al firma
     * @throws AlmacenDocumentoTramitacionException: Si ocurre alg�n error
     */
    public String getFirmaDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{         
         String firma = null;
         
         try{
            DocumentoGestor docGestor = (DocumentoGestor)doc;             
            CachePropiedadesConfiguracionGestor cache = CachePropiedadesConfiguracionGestor.getInstance(doc.getCodMunicipio(),getNombreServicio());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setCarpetaRaiz(cache.getCarpetaRaiz());
            docGestor.setImplClassGestor(cache.getImplClassGestor());
            docGestor.setLongitudCodVisibleTramiteBD(cache.getLongitudCodigoTramiteVisible());
            docGestor.setLongitudNumeroDocumentoBD(cache.getLongitudNumeroDocumento());
            docGestor.setLongitudOcurrenciaTramiteInternoBD(cache.getLongitudOcurrenciaTramiteInterno());
            docGestor.setPaswordGestor(cache.getPasswordGestor());
            docGestor.setUrlGestor(cache.getUrlGestor());
            docGestor.setUsuarioGestor(cache.getUsuarioGestor());
            
            Class implClass          = Class.forName(docGestor.getImplClassGestor());
            GestorDocumental gestor  =(GestorDocumental) implClass.newInstance();                                        
            docGestor.setNombreFicheroCompleto(docGestor.getCodDocumento() + "-" + docGestor.getCodTramite() + "-" + docGestor.getOcurrenciaTramite() + "-" + docGestor.getNombreDocumento() + ConstantesDatos.DOT + ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
            byte[] contenido = gestor.getDocumento(docGestor);
            firma = new String(contenido);
             
         }catch(Exception e){
             e.printStackTrace();
             throw new AlmacenDocumentoTramitacionException(2,"Error al recupera la firma de un documento externo asociado a una notificaci�n: " + e.getMessage());             
         }
         
         return firma;
     }
      
    public void setDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarDocumentoDuplicado(DocumentoFirma doc, String[] params) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarTodosDocumentosTramite(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
      
}
