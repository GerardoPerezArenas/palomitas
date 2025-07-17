package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasDAO;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.ExpedienteOtroDocumentoManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.exception.TechnicalDocumentoException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * Plugin de almacenamiento de documentos en un gestor documental
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoTramitacionGestor implements AlmacenDocumento{
    private Logger log = Logger.getLogger(AlmacenDocumentoTramitacionGestor.class);

    private final String GUION = "-";
    private final String GUION_BAJO = "_";
    private final String DOT    = ".";
    private final String CERO  = "0";
    private final String ELEMENTO_REEMPLAZAR = "@";
    private final String SUFIJO_EXT = "EXT";

    public String getNombreServicio(){
        return ConstantesDatos.PLUGIN_ALMACENAMIENTO_BBDD;        
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
     * Devuelve el nombre que tendrá un document en el gestor documental
     * @param doc: Objeto con toda la información relativa al documento
     * @param nombre: Nombre del documento o fichero
     * @return Nombre del documento completo
     */
    private String getNombreFicheroCompleto(Documento doc,String nombre){

        StringBuffer nuevoNombreCompletoFichero = new StringBuffer();
        DocumentoGestor docGestor = (DocumentoGestor)doc;

         /**************** NUEVO NOMBRE DEL FICHERO A MODIFICAR *****/
        // Se establece el nuevo nombre que tendrá el documento en el gestor si es que se modifica
        int longitudNumeroDocumento = docGestor.getNumeroDocumento().length();
        int longitudOcuTramite = Integer.toString(docGestor.getOcurrenciaTramite()).length();
        int longitudCodTramite = Integer.toString(docGestor.getCodTramiteVisible()).length();

        nuevoNombreCompletoFichero.append(nombre);
        nuevoNombreCompletoFichero.append(GUION);

        for(int i=0;i<(docGestor.getLongitudCodVisibleTramiteBD() - longitudCodTramite);i++){
            nuevoNombreCompletoFichero.append(CERO);
        }
        nuevoNombreCompletoFichero.append(docGestor.getCodTramiteVisible());
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

        
        // Se establece el nuevo nombre que tendrá el documento en el gestor si es que se modifica
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
      * Reemplaza los caracteres especiales que se encuentran en el array indicado por el parámetro caracteres en
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


    /**
     * Recupera la lista de caracters no permitidos por el gestor documental en los nombres de carpetas y de documentos
     * @param codMunicipio: Código del municipio
     * @return String[]
     */
    private String[] getListaCaracteresNoPermitidos(int codMunicipio){
        String[] salida = null;
        ResourceBundle documentosConfig = ResourceBundle.getBundle("documentos");

        String tipoPlugin   = documentosConfig.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
        String nombreGestor = documentosConfig.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
        String carpetaRaiz  = documentosConfig.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
        log.debug("tipoPlugin: " + tipoPlugin + ", nombreGestor: " + nombreGestor + ", carpetaRaiz: " + carpetaRaiz);

        String caracteresNoPermitidos = documentosConfig.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + nombreGestor
                                           + ConstantesDatos.BARRA + "caracteres_no_permitidos");

        if(caracteresNoPermitidos!=null && !"".equals(caracteresNoPermitidos))
            salida = caracteresNoPermitidos.split(ConstantesDatos.DOT_COMMA);

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
        
        /*** SE TRATA LA LISTA DE CARPETAS EN LAS QUE SE ALOJA EL DOCUMENTO RETIRANDO LOS CARACTERES ESPECIALES *****/

        if(!docGestor.isDocRelacion()){
                // No se trata de un documento de una relación de expediente
                gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());
                if(docGestor.getNumeroDocumento()!=null && !"".equals(docGestor.getNumeroDocumento()))
                {
                    try{
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();

                        // Se recupera el nombre del documento que tenía originalmente porque puede que sea modificado
                        String nombreOriginalDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);
                        String nombreNuevoDocumento = docGestor.getNombreDocumento();

                        // Nombre original  del documento
                        docGestor.setNombreDocumento(nombreOriginalDocumento);
                        // Nombre original del document en el gestor
                        /** original
                            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento));
                         */                        
                        docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento), listaCaracteresNoPermitidos));
                        // Nuevo nombre del fichero
                        /** original
                        docGestor.setNuevoNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreNuevoDocumento));
                         */
                        docGestor.setNuevoNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreNuevoDocumento), listaCaracteresNoPermitidos));

                        if(gestor.modificarDocumento(docGestor)){                        
                            // Se modifica el documento
                            DocumentosExpedienteManager.getInstance().grabarDocumentoGestor(gVO, params);
                            exito = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                    }

                }else{

                    try{
                        Class implClass = Class.forName(docGestor.getImplClassGestor());
                        GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                        // Se graba el documento por primera vez
                        // Se guardan los datos del documento en base de datos
                        int numeroDocumento = DocumentosExpedienteManager.getInstance().grabarDocumentoGestor(gVO, params);
                        if(numeroDocumento>0){
                            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));
                            /** original
                            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                            */
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
                                // El documento a eliminar no pertenece a una relación
                                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                                exito = true;
                            }else{
                                // TODO: VERIFICAR CUANDO EL DOCUMENTO PERTENECE A UNA RELACIÓN
                                tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                                // El documento a eliminar pertenece a una relación
                                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                                if(resultado>0) exito = true;
                            }
                            /********************************************************/
                            throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                     }//catch
                }// else
            }else{
                /***************  SI EL DOCUMENTO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES **********************/
               // No se trata de un documento de una relación de expediente
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
                        /** original
                        docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento));
                         */
                        docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento),listaCaracteresNoPermitidos));
                        // Nuevo nombre del fichero a modificar
                        /** original
                        docGestor.setNuevoNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreDocumentoNuevo));
                         */
                        docGestor.setNuevoNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,nombreDocumentoNuevo),listaCaracteresNoPermitidos));
                                                
                        if(gestor.modificarDocumento(docGestor)){                        
                            // Se modifica el documento
                            DocumentosRelacionExpedientesManager.getInstance().grabarDocumentoGestor(gVO, params);
                            exito = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
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
                            /** original
                            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                            */
                            docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                           // Se guarda el documento en el gestor y se recupera la url del documento en el gestor
                           String url = gestor.setDocumento(docGestor);                            
                           // Para los documentos presentes en la relación se compone  un nuevo fichero HTML con los expedientes relacionados.
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

                           /** Se procede a almacenar el documento html en el gestor con el listado de expedientes pertenecientes a la relación */
                           docGestor.setFichero(contenidoHTML.toString().getBytes());
                           docGestor.setExtension("html");
                           String sNumRelacion = docGestor.getNumeroRelacion().replaceAll("/",GUION);
                           docGestor.setNombreDocumento(ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion);                           
                           docGestor.setNombreFicheroCompleto(ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion);                            
                           docGestor.setTipoMimeContenido(ConstantesDatos.TIPO_MIME_HTML);
                           docGestor.setCodificacionContenido(ConstantesDatos.CODIFICACION_UTF_8);

                           boolean exitoListaRelacion = gestor.setListaExpedientesRelacion(docGestor);
                           if(exitoListaRelacion)
                               log.debug(" ******************* Se guardado la lista de expedientes en la relación");
                           else
                               log.debug(" ******************* No se guardado la lista de expedientes en la relación");
                           
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
                                // El documento a eliminar no pertenece a una relación
                                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                                exito = true;
                            }else{
                                // TODO: VERIFICAR CUANDO EL DOCUMENTO PERTENECE A UNA RELACIÓN
                                tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                                // El documento a eliminar pertenece a una relación
                                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                                if(resultado>0) exito = true;
                            }
                            /********************************************************/
                            throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                     }//catch
                }// else



            }
         return exito;
    }






    public byte[] getDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;
        GeneralValueObject gVO = new GeneralValueObject();

        DocumentoGestor docGestor = (DocumentoGestor)doc;
      
        try{

            // Si el documento no pertenece a una relación de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
           /** original
           docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
            **/

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
                // EL DOCUMENTO NO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES                
                Class implClass = Class.forName(docGestor.getImplClassGestor());
                GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
                /** original
                docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                 */
                docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                if(gestor.eliminarDocumento(docGestor)){                
                    tEVO.setNumeroExpediente(docGestor.getNumeroExpediente());
                    // El documento a eliminar no pertenece a una relación
                    TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                    exito = true;
                }else{
                        throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental");
                }

            }else{
                  // EL DOCUMENTO NO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES
                Class implClass = Class.forName(docGestor.getImplClassGestor());
                /** original
                docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                */
                docGestor.setNombreFicheroCompleto(sustituirCaracteresEspeciales(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()),listaCaracteresNoPermitidos));

                // Se borra la referencia al documento de la BBDD pero no del gestor
                //if(gestor.eliminarDocumento(doc)){
                    tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                    int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                    if(resultado>0) exito = true;
                //}else{
                //        throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental");
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
           throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental: " + e.getMessage());
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
            /** original
            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()));
             */
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
    
    public Documento getDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        try{

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


            // Si el documento no pertenece a una relación de expedientes
           Class implClass = Class.forName(docGestor.getImplClassGestor());
           GestorDocumental gestor =(GestorDocumental) implClass.newInstance();
           /** original
           docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()));
            */

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

            int codMunicipio          = doc.getCodMunicipio();
            int ejercicio                = doc.getEjercicio();
            String numExpediente = doc.getNumeroExpediente();
            String numeroDocumento = doc.getNumeroDocumento();
            String[] params               = doc.getParams();

            // Si el documento no pertenece a una relación de expedientes
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

            /** ORIGINAL
            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()));
            */           
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

            /** original
            docGestor.setNombreFicheroCompleto(codigoDocumento + ConstantesDatos.GUION_BAJO + nombreDocumento + ConstantesDatos.DOT + extension);
             */
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
                    // Si no se ha podido guardar el documento en Alfresco se deshace la transacción
                    e.printStackTrace();
                    exito = false;
                    try{
                        // Se aborta la transacción
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
                    // Se aborta la transacción
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
                // Se aborta la transacción
                adapt.rollBack(connection);
            }catch(BDException f){
                f.printStackTrace();
            }
            
        }

        return exito;
    }

    public boolean setFirmaDocumentoExpediente(DocumentoProcedimientoFirmaVO doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DocumentoProcedimientoFirmaVO getDocumentoExpediente(DocumentoProcedimientoFirmaVO doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    // Da de alta la firma de un documento externo asociado a una notificación electrónica
    public boolean setFirmaDocumentoExternoNotificacion(AdjuntoNotificacionVO doc) throws AlmacenDocumentoTramitacionException{        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public Documento getDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getDocumento(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setFirmaDocumentoExpediente(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setFirmaDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Documento getDocumentoExternoNotificacion(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setDocumentoDatoSuplementarioExpediente(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setDocumentoDatoSuplementarioTramite(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDocumentosRegistro(ArrayList<Documento> documentos, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Documento getDocumentoDatosSuplementarios(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Documento getDocumentoDatosSuplementariosTramite(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarDocumentoDatosSuplementarios(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Documento getDocumentoRegistro(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarDocumentoRegistro(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarTodosDocumentosDatosSuplementariosTramite(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Documento getDocumentoRegistroConsulta(Documento doc, String origen) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getFirmaDocumentoExternoNotificacion(Documento doc, Connection con) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setDocumentoExternoFicheroCSV(Documento doc) throws AlmacenDocumentoTramitacionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
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