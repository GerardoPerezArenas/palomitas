package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.ExpedienteOtroDocumentoManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.exception.TechnicalDocumentoException;
import es.altia.agora.business.sge.plugin.documentos.sharepoint.ws.nextret.ClienteWSNextRetSharepoint;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoTramitacionSharePointNextRet implements AlmacenDocumento{

    private Logger log = Logger.getLogger(AlmacenDocumentoAlfrescoImpl.class);
    private final String GUION = "-";
    private final String GUION_BAJO = "_";
    private final String DOT    = ".";
    private final String CERO  = "0";
    private final String ELEMENTO_REEMPLAZAR = "@";
    private final String SUFIJO_EXT = "EXT";
    private String tipoPlugin = null;
        
    public String getNombreServicio(){
        return ConstantesDatos.PLUGIN_ALMACENAMIENTO_NEXTRET;
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
        try{
            log.debug("@@@@@@@@@@@> setFirmaDocumento <@@@@@@@@@ ");
            log.debug("@@@@@@@@@@@> es documento de relación:  " + doc.isDocRelacion());
            ClienteWSNextRetSharepoint cliente = new ClienteWSNextRetSharepoint();
            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
            if(doc.isDocRelacion())  docGestor.setNumeroRelacion("REL_" + docGestor.getNumeroRelacion());

            log.debug("@@@@@@@@@@@ guardando el nombre el fichero de firma, getNombreFicheroCompleto: " + docGestor.getNombreFicheroCompleto());
            boolean exitoGestor = cliente.setDocumento(docGestor);
            if(exitoGestor) exito = true;
            
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

        private void eliminaDocAnteFallo(DocumentoGestor docGestor){
            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
            tEVO.setCodMunicipio(Integer.toString(docGestor.getCodMunicipio()));
            tEVO.setCodProcedimiento(docGestor.getCodProcedimiento());
            tEVO.setEjercicio(Integer.toString(docGestor.getEjercicio()));
            tEVO.setCodTramite(Integer.toString(docGestor.getCodTramite()));
            tEVO.setOcurrenciaTramite(Integer.toString(docGestor.getOcurrenciaTramite()));


            if(!docGestor.isDocRelacion()){
                log.debug("======= se eliminar un documenbto de tramitación porque no se ha podido grabar en el gestor sharepoint");
                String numExpediente = docGestor.getNumeroExpediente().replace("-","/");
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodDocumento(Integer.toString(docGestor.getCodDocumento()));
                // El documento a eliminar no pertenece a una relación
                TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());                
            }
        }


    public boolean setDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
         boolean exito = false;
         log.debug(this.getClass().getName() + ".setDocumento init");

        ClienteWSNextRetSharepoint cliente = new ClienteWSNextRetSharepoint();
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

        log.debug("@@@@@@@@@@ Plugin.setDocumento isRelacion : " + docGestor.isDocRelacion());
        if(!docGestor.isDocRelacion()){
                // No se trata de un documento de una relación de expediente
                gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());

                log.debug("=========> Plugin el documento no es de una relación");
                log.debug("=========> Plugin docGestor.getNumeroDocumento(): " + docGestor.getNumeroDocumento());

                if(docGestor.getNumeroDocumento()!=null && !"".equals(docGestor.getNumeroDocumento()))
                {
                    try{
                        log.debug("=========> setDocumento se graba un documento existente numero de documento: " + docGestor.getNumeroDocumento());

                        // Se recupera el nombre del documento que tenía originalmente porque puede que sea modificado
                        String nombreOriginalDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);
                        String nombreNuevoDocumento = docGestor.getNombreDocumento();

                        log.debug(" ======> Plugin.setDocumento nombreOriginalDocumento: " + nombreOriginalDocumento);
                        log.debug(" ======> Plugin.setDocumento nombreNuevoDocumento: "   + nombreNuevoDocumento);

                        // Nombre original  del documento
                        docGestor.setNombreDocumento(nombreOriginalDocumento);
                        // Nombre original del document en el gestor
                        docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento));
                        // Nuevo nombre del fichero
                        docGestor.setNuevoNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreNuevoDocumento));

                        log.debug(" ======> Plugin.setDocumento docGestor.getNombreFicheroCompleto(): "   + docGestor.getNombreFicheroCompleto());
                        log.debug(" ======> Plugin.setDocumento docGestor.getNuevoNombreFicheroCompleto(): "   + docGestor.getNuevoNombreFicheroCompleto());
                        
                        
                        // El documento existe, por tanto se modifica, lo que se hace es sobreescribirlo                        
                        //docGestor.setNombreFicheroCompleto(docGestor.getNuevoNombreFicheroCompleto());
                        log.debug("=======> Antes de llamar a la operación de modificarDocumento de ClienteWSNextRetSharepoint");
                        boolean exitoGestor = cliente.modificarDocumento(docGestor);
                        docGestor.setNombreFicheroCompleto(docGestor.getNuevoNombreFicheroCompleto());
                        // Se convierte el número del expediente en el adecuado con la / como separador entres sus componentes
                        docGestor.setNumeroExpediente(cliente.tratarNumeroExpediente(docGestor.getNumeroExpediente(),"-","/"));
                        log.debug("=====> AlmacenDocumentoTramitacionsharePoint exito operacion alta documento en sharepoint " + exitoGestor);
                        if(exitoGestor){
                            // Si se ha guardado el documento en el gestor se procede a guardarlo en base de datos
                            int salida = DocumentosExpedienteManager.getInstance().grabarDocumento(gVO, params);
                            if(salida>0) exito = true;
                        }                     

                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                    }

                }else{

                    log.debug(" =====> setDocumento se graba un documento nuevo");
                    int numeroDocumento = 0;
                    try{

                        // Se graba el documento por primera vez
                        /****** LISTO ******/
                        docGestor.setNumeroExpediente(cliente.tratarNumeroExpediente(docGestor.getNumeroExpediente(),"-","/"));
                        log.debug(" =====================> A grabar el documento que no existe ");
                        // Se graba el registro correspondiente en base de datos y también se graba el documento.
                        numeroDocumento = DocumentosExpedienteManager.getInstance().grabarDocumentoGestorContenido(gVO, params);
                        log.debug(" =========> setDocumento se ha dado de alta el documento con numero " + numeroDocumento);
                        docGestor.setCodDocumento(numeroDocumento);
                        docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));
                        docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                        log.debug(" =====================> El documento ha sido grabado " + numeroDocumento);
                        if(numeroDocumento>0){                            
                            exito = cliente.setDocumento(docGestor);
                            if (!exito){
                                this.eliminaDocAnteFallo(docGestor); 
                            }
                        }

                        /****** LISTO ******/
                      
                    }catch(Exception e){
                            exito = false;
                            e.printStackTrace();
                            log.debug("ERROR AL DAR DE ALTA EL DOCUMENTO :  " + this.getClass().getName() + ".setDocumento: " + e.getMessage());
                            log.debug("SE PROCEDE A REALIZAR EL BORRADO DEL MISMO");
                            /******************  SI SE PRODUCE UN FALLO AL ALMACENAR EL DOCUMENTO EN EL GESTOR, HAY QUE ELIMINAR LA REFERENCIA AL DOCUMENTO EN EL GESTOR   ******************/                            
                            this.eliminaDocAnteFallo(docGestor);                           
                            throw new AlmacenDocumentoTramitacionException(1,"Error al grabar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                     }//catch
                }// else
            }else{
                /***************  SI EL DOCUMENTO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES **********************/
               // No se trata de un documento de una relación de expediente
                //gVO.setAtributo("numeroExpediente",docGestor.getNumeroExpediente());
               gVO.setAtributo("opcionGrabar", docGestor.getOpcionGrabar());
               gVO.setAtributo("numeroRelacion", docGestor.getNumeroRelacion());

                log.debug(this.getClass().getName() + ",  es un documento de relación");
                if(docGestor.getNumeroDocumento()!=null && !"".equals(docGestor.getNumeroDocumento()))
                { log.debug(this.getClass().getName() + ", el documento existe");
                    try{

                        String nombreOriginalDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, params);
                        String nombreDocumentoNuevo = docGestor.getNombreDocumento();

                        // Nombre original  del documento
                        docGestor.setNombreDocumento(nombreOriginalDocumento);
                        // Nombre original del document en el gestor
                        docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreOriginalDocumento));
                        // Nuevo nombre del fichero a modificar
                        docGestor.setNuevoNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,nombreDocumentoNuevo));

                        // El documento existe, por tanto se modifica, lo que se hace es sobreescribirlo                        
                        //docGestor.setNombreFicheroCompleto(docGestor.getNuevoNombreFicheroCompleto());
                        docGestor.setNumeroRelacion("REL_" + docGestor.getNumeroRelacion());
                        boolean exitoGestor = cliente.modificarDocumento(docGestor);

                        if(exitoGestor){
                            // Si se ha guardado el documento en el gestor se procede a guardarlo en base de datos junto con el contenido del documento
                            //int salida = DocumentosRelacionExpedientesManager.getInstance().grabarDocumento(gVO, params);
                            int salida = DocumentosRelacionExpedientesManager.getInstance().grabarDocumentoContenido(gVO, params);
                            if(salida>0) exito = true;
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        exito = false;
                        throw new AlmacenDocumentoTramitacionException(6,"Error al modificar el documento de tramitación " + docGestor.getNombreDocumento() + "en el gestor documental: " + e.getMessage());
                    }

                }else{

                    log.debug(" @@@@@@@@@@@@ GRABANDO DOCUMENTO DE RELACIÓN QUE NO EXISTE ***********************");
                    log.debug(" @@@@@@@@@@@@ NOMBRE FICHERO COMPLETO getNombreFicheroCompleto: " + docGestor.getNombreFicheroCompleto());
                    log.debug(" ********************** NOMBRE FICHERO COMPLETO getNombreDocumento: " + docGestor.getNombreDocumento());
                    log.debug(" ********************** NOMBRE FICHERO COMPLETO getNumeroRelacion: " + docGestor.getNumeroRelacion());
                    int numeroDocumento =0;
                    try{
                        // Se graba el documento por primera vez
                        // Se guardan los datos del documento en base de datos
                        log.debug(this.getClass().getName() + ", es de relación y no existe");
                        /*** PRUEBA ***/                    
                        numeroDocumento = DocumentosRelacionExpedientesManager.getInstance().grabarDocumentoContenido(gVO, params);
                        
                        if(numeroDocumento>0){
                            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));
                            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                            // Se guarda el documento en el gestor y se recupera la url del documento en el gestor

                            docGestor.setNumeroRelacion("REL_" + docGestor.getNumeroRelacion());
                            log.debug(" ********************** ANTES DE GUARDAR DOCUMENTO DE RELACIÓN EN EL GESTOR ");
                            log.debug(" ********************** NOMBRE DEL FICHERO COMPLETO PARA ALMACENAR EN EL GESTOR "
                                    + docGestor.getNombreFicheroCompleto());

                            /***** DESCOMENTAR. SOLO PARA PRUEBAS EN LOCAL ***/
                            cliente.setDocumento(docGestor);

                            log.debug(" ********************** DESPUÉS DE GUARDAR DOCUMENTO DE RELACIÓN EN EL GESTOR ");
                            /** SE GUARDA UN FICHERO HTML CUYO CONTENIDO INDICA LOS EXPEDIENTES QUE FORMAN PARTE DE LA RELACIÓN **/
                           // Para los documentos presentes en la relación se compone  un nuevo fichero HTML con los expedientes relacionados.
                           ResourceBundle bundle = ResourceBundle.getBundle("documentos") ;
                           String cabeceraHTML = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_CABECERA_HTML_RELACION);
                           String cuerpoHTML    = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_CUERPO_HTML_RELACION);
                           String pieHTML         = bundle.getString(ConstantesDatos.PROP_ALMACEN_GESTOR_PIE_HTML_RELACION);

                           StringBuffer contenidoHTML = new StringBuffer();
                           contenidoHTML.append(cabeceraHTML);
                           for(int i=0;docGestor.getListaExpedientes()!=null && i<docGestor.getListaExpedientes().size();i++){
                                String expediente = docGestor.getListaExpedientes().get(i);
                                log.debug(" =======> Expediente a tratar: " + expediente);
                                String dExp = cuerpoHTML;
                                contenidoHTML.append(dExp.replaceFirst(ELEMENTO_REEMPLAZAR,expediente));
                                contenidoHTML.append(pieHTML);
                           }// for


                           log.debug(" ********************** El contenido del fichero HTML es: " + contenidoHTML.toString());

                           // Se procede a almacenar el documento html en el gestor con el listado de expedientes pertenecientes a la relación
                           docGestor.setFichero(contenidoHTML.toString().getBytes());
                           docGestor.setExtension("html");
                           String sNumRelacion = docGestor.getNumeroRelacion().replaceAll("/",GUION);
                           docGestor.setNombreDocumento(ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion);
                           String nombreFichero = ConstantesDatos.NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION + sNumRelacion + "." + docGestor.getExtension();
                           log.debug(" ***** nombre fichero html con la lista de expedientes que forman parte de la relación : " + nombreFichero);
                           docGestor.setNombreFicheroCompleto(nombreFichero);
                           docGestor.setTipoMimeContenido(ConstantesDatos.TIPO_MIME_HTML);
                           docGestor.setCodificacionContenido(ConstantesDatos.CODIFICACION_UTF_8);

                           log.debug(" ********************** ANTES DE GUARDAR EL DOCUMENTO HTML EN EL GESTOR getNombreFicheroCompleto " +
                                  docGestor.getNombreFicheroCompleto());


                           boolean exitoListaRelacion = cliente.setDocumento(docGestor);
                           if(exitoListaRelacion){
                               log.debug(" ========== Se guardado la lista de expedientes en la relación");
                               exito = true;
                           }
                           else
                               log.debug(" ========== No se guardado la lista de expedientes en la relación");


                        }

                        /*** PRUEBA ***/

                     
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
                                String[] dNumeroRelacion = docGestor.getNumeroRelacion().split("REL_");
                                String numeroRelacion = "";
                                if(dNumeroRelacion!=null && dNumeroRelacion.length==2)
                                    numeroRelacion = dNumeroRelacion[1].replace(ConstantesDatos.GUION,ConstantesDatos.BARRA);

                                tEVO.setNumeroRelacion(numeroRelacion);
                                tEVO.setCodDocumento(Integer.toString(numeroDocumento));
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


    /** Este método es igual que el método getDocumento del plugin de base de datos ya que actualmente no se puede recuperar el contenido del documento
     * a través del WS de Listas
     * @param doc: Documento
     * @return byte[] con el contenido del documento
     */
      public byte[] getDocumento(Documento doc) throws AlmacenDocumentoTramitacionException{
        byte[] contenidoDocumento = null;
        GeneralValueObject gVO = new GeneralValueObject();

        DocumentoGestor docBD = (DocumentoGestor)doc;

        gVO.setAtributo("codMunicipio", Integer.toString(docBD.getCodMunicipio()));
        gVO.setAtributo("codTramite",Integer.toString(docBD.getCodTramite()));
        gVO.setAtributo("ocurrenciaTramite",Integer.toString(docBD.getOcurrenciaTramite()));
        gVO.setAtributo("numeroDocumento",docBD.getNumeroDocumento());

        log.debug("====================> " + this.getClass().getName() + ".getDocumento() codMunicipio: " + (String)gVO.getAtributo("codMunicipio"));
        log.debug("====================> " + this.getClass().getName() + ".getDocumento() codTramite: " + (String)gVO.getAtributo("codTramite"));
        log.debug("====================> " + this.getClass().getName() + ".getDocumento() ocurrenciaTramite: " + (String)gVO.getAtributo("ocurrenciaTramite"));
        log.debug("====================> " + this.getClass().getName() + ".getDocumento() numeroDocumento: " + (String)gVO.getAtributo("numeroDocumento"));

        if(!docBD.isDocRelacion()){
            // Si el documento no pertenece a una relación de expedientes
            gVO.setAtributo("numeroExpediente",docBD.getNumeroExpediente());
            log.debug("====================> " + this.getClass().getName() + ".getDocumento() antes de recuperar contenido del documento de base de datos ");
            contenidoDocumento =  DocumentosExpedienteManager.getInstance().loadDocumento(gVO, docBD.getParams());
            log.debug("====================> " + this.getClass().getName() + ".getDocumento() después de recuperar contenido de la BD " + contenidoDocumento.length);
        }else{
            // Si el documento pertenece a una relación de expedientes
            gVO.setAtributo("numeroRelacion",docBD.getNumeroRelacion());
            log.debug("====================> " + this.getClass().getName() + ".getDocumento() antes de recuperar contenido del documento de relación de base de datos ");
            contenidoDocumento = DocumentosRelacionExpedientesManager.getInstance().loadDocumento(gVO,docBD.getParams());
            log.debug("====================> " + this.getClass().getName() + ".getDocumento() después de recuperar contenido documento de relación de la BD " + contenidoDocumento.length);
        }
        return contenidoDocumento;
    }

      
      
     public byte[] getDocumento(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
          throw new UnsupportedOperationException("Not supported yet");
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

            if(!docGestor.isDocRelacion()){
                // EL DOCUMENTO NO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES                
               docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));

               ClienteWSNextRetSharepoint gestor = new ClienteWSNextRetSharepoint();

                if(gestor.eliminarDocumento(docGestor)){
                    docGestor.setNumeroExpediente(gestor.tratarNumeroExpediente(docGestor.getNumeroExpediente(),"-","/"));
                    tEVO.setNumeroExpediente(docGestor.getNumeroExpediente());
                    // El documento a eliminar no pertenece a una relación
                    int num = TramitacionExpedientesManager.getInstance().eliminarDocumentoCRD(tEVO,docGestor.getParams());
                    if(num==1)
                        exito = true;
                }else{
                        throw new AlmacenDocumentoTramitacionException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental");
                }

            }else{
                  // EL DOCUMENTO NO PERTENECE A UNA RELACIÓN ENTRE EXPEDIENTES
                docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompleto(doc,docGestor.getNombreDocumento()));
                // Se borra la referencia al documento de la BBDD pero no del gestor
                tEVO.setNumeroRelacion(docGestor.getNumeroRelacion());
                int resultado = TramitacionExpedientesManager.getInstance().eliminarDocumentoRelacionCRD(tEVO,docGestor.getParams());
                if(resultado>0) exito = true;

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
            ExpedienteOtroDocumentoVO docExterno = new ExpedienteOtroDocumentoVO();
            docExterno.setMunicipio(Integer.toString(codMunicipio));
            docExterno.setEjercicio(Integer.toString(ejercicio));
            docExterno.setNumeroExpediente(numExpediente);
            docExterno.setContenidoDocumento(null);
            docExterno.setTipoDocumento(tipoMime);
            docExterno.setNombreDocumento(nombreDoc);
            docExterno.setExtension(extension);
            // ====> Será necesario guardar en base de datos el contenido del documento externo asociado al expediente ya que no se podrá recuperar de SharePoint
            docExterno.setContenidoDocumento(fichero);

            // SE DA DE ALTA EL REGISTRO CORRESPONDIENTE AL DOCUMENTO EXTERNO ASOCIADO AL EXPEDIENTE, PERO SERÁ NECESARIO GUARDARLO EN BBDD APARTE
            // DE DARLO DE ALTA EN EL GESTOR, PORQUE EL CAMPO
            numeroDocumento = ExpedienteOtroDocumentoManager.getInstance().altaDocumento(docExterno, params);
            docGestor.setNumeroDocumento(Integer.toString(numeroDocumento));
            docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()));


            if(numeroDocumento!=-1){
                // Se ha dado de alta en la bd la referencia al documento externo
                ClienteWSNextRetSharepoint cliente = new ClienteWSNextRetSharepoint();
                boolean exitoGestor = cliente.setDocumento(docGestor);
                if(exitoGestor) exito = true;
            }else throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental");

            /* ORIGINAL ALFRESCO
            if(numeroDocumento!=-1){
                // Se ha dado de alta en la bd la referencia al documento externo
                gestor.setDocumento(docGestor);
                exito = true;
            }else throw new AlmacenDocumentoTramitacionException(8,"Error al grabar el documento externo en el gestor documental");
            */
        }catch(TechnicalDocumentoException e){
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

    /**
     * Recupera el contenido de un documento externo de la base de datos y no del gestor.
     * @param doc: Objeto con la información necesario del documento para recuperar el contenido del documento de la base de datos
     * @return Documento
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException
     */
     public Documento getDocumentoExterno(Documento doc) throws AlmacenDocumentoTramitacionException{
        DocumentoGestor docBD = (DocumentoGestor)doc;
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
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        try{

            int codMunicipio          = doc.getCodMunicipio();
            int ejercicio                = doc.getEjercicio();
            String numExpediente = doc.getNumeroExpediente();
            String numeroDocumento = doc.getNumeroDocumento();
            String[] params               = doc.getParams();

            // Si el documento no pertenece a una relación de expedientes
           ClienteWSNextRetSharepoint gestor = new ClienteWSNextRetSharepoint();
           docGestor.setNombreFicheroCompleto(this.getNombreFicheroCompletoExterno(doc,docGestor.getNombreDocumento()));

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

        
        }catch(TechnicalDocumentoException e){
            e.printStackTrace();
            log.error(this.getClass().getName() + ".getDocumento: " + e.getMessage());
            throw new AlmacenDocumentoTramitacionException(5,"No se puede eliminar el documento del gestor documental");
        }

        return exito;
    }



 

  /**
     * Para la primera versión del plugin no se implementa esta operación  ya que no se hará uso en un principio del servicio web de envio de documentos al portafirmas
     * @param doc: DocumentoOtroFirmaVO con la información del documento a recuperar
     * @return DocumentoOtroFirma con la información completa del documento
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException
     */
    public DocumentoOtroFirmaVO getDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{
        DocumentoOtroFirmaVO salida = new DocumentoOtroFirmaVO();
        return salida;
    }


  /**
     * Para la primera versión del plugin no se implementa esta operación  ya que no se hará uso en un principio del servicio web de envio de documentos al portafirmas
     * @param doc: DocumentoOtroFirmaVO con la información del documento a recuperar
     * @return DocumentoOtroFirma con la información completa del documento
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException
     */
    public boolean setFirmaDocumentoExternoPortafirmas(DocumentoOtroFirmaVO doc) throws AlmacenDocumentoTramitacionException{       
        return true;
    }


    // Alta de un documento de expediente que ha sido presentado
    public boolean setDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
          boolean exito = false;
        DocumentoGestor docGestor =  (DocumentoGestor)doc;
        try{
            
            Documento docBBDD = doc;            
            boolean continuar = false;
            if(!doc.isModificarAdjuntoDocExpediente()) // Se da de alta el adjunto asociado al expediente pero sin el contenido
                continuar = ExpedienteDocPresentadoManager.getInstance().setDocumentoPresentado(docBBDD);
            else // Si se modifica el contenido del adjunto  se
                continuar = ExpedienteDocPresentadoManager.getInstance().modificarDocumentoPresentado(docBBDD);

            if(continuar){
                // Si se ha podido guardar el correspondiente registro en base de datos, se procede a dar de alta el contenido o bien a modificarlo
                // en el gestor documenta
                docGestor.setNombreFicheroCompleto("INICIO_" + docGestor.getNombreFicheroCompleto());
                ClienteWSNextRetSharepoint gestor = new ClienteWSNextRetSharepoint();

                if(!doc.isModificarAdjuntoDocExpediente())
                   gestor.setDocumento(docGestor);
                else{
                    docGestor.setNuevoNombreFicheroCompleto("INICIO_" + docGestor.getNuevoNombreFicheroCompleto());
                    gestor.modificarDocumento(docGestor);
                }
            }

        } catch(TechnicalDocumentoException e){
            // No se ha podido dar de alta el documento externo en el gestor => Hay que eliminar la referencia al documento en base de datos
            e.printStackTrace();
            if(!docGestor.isModificarAdjuntoDocExpediente()){ // Se da de alta el adjunto asociado al expediente pero sin el contenido
                docGestor.setNumeroExpediente(docGestor.getNumeroExpediente().replace(ConstantesDatos.GUION,ConstantesDatos.BARRA));
                ExpedienteDocPresentadoManager.getInstance().eliminarDocumentoPresentado(docGestor);
            }
            throw new AlmacenDocumentoTramitacionException(12,"Error al grabar el adjunto asociado a un documento de expediente en el gestor documental: "  + e.getMessage());
        }

        return exito;
    }

    // Recupera el contenido de un documento de expediente que ha sido presentado
    public Documento getDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        Documento salida = null;
        log.debug("===> getDocumentoPresentado ");
        doc.setNumeroExpediente(doc.getNumeroExpediente().replace(ConstantesDatos.GUION,ConstantesDatos.BARRA));
        salida = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
        return salida;
    }
    
    // Elimina un documento de expediente que ha sido presentado
    public boolean eliminarDocumentoPresentado(Documento doc) throws AlmacenDocumentoTramitacionException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;
        boolean eliminadoDocGestor = false;
        
        try{
            ClienteWSNextRetSharepoint gestor = new ClienteWSNextRetSharepoint();
            docGestor.setNombreFicheroCompleto("INICIO_" + docGestor.getNombreFicheroCompleto());
            eliminadoDocGestor = gestor.eliminarDocumento(docGestor);
            log.debug("Eliminado el documento presentado del gestor documental");
            if(eliminadoDocGestor){
                docGestor.setNumeroExpediente(docGestor.getNumeroExpediente().replace(ConstantesDatos.GUION,ConstantesDatos.BARRA));
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

    public boolean setFirmaDocumentoExpediente(Documento doc) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

       
    // Da de alta la firma de un documento externo asociado a una notificación electrónica
    public boolean setFirmaDocumentoExternoNotificacion(AdjuntoNotificacionVO doc) throws AlmacenDocumentoTramitacionException{        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public Documento getDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("Not supported yet.");
    }
     
   public boolean setDocumentoDatoSuplementarioTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("Not supported yet.");
   }
 
   
   public boolean setDocumentosDatosSuplementariosTramite(Connection con, Vector docs,  String[] params) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("Not supported yet.");
   }   
 
   
   public void setDocumentosRegistro(ArrayList<Documento> documentos,Connection con) throws AlmacenDocumentoTramitacionException{
       throw new UnsupportedOperationException("Not supported yet.");
   }
   
   
   public Documento getDocumentoRegistro(Documento doc) throws AlmacenDocumentoTramitacionException{
      throw new UnsupportedOperationException("Not supported yet.");
   }

    // Recupera un documento de los datos suplementarios de tipo fichero de expediente
    public Documento getDocumentoDatosSuplementarios(Documento doc) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");
    }

    // Recupera un documento de los datos suplementarios del tipo fichero de tramite
    public Documento getDocumentoDatosSuplementariosTramite(Documento doc) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");
    }
   

    // Elimina un documento de los datos suplementarios de expediente
     public boolean eliminarDocumentoDatosSuplementarios (Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");        
    }
    
    // Elimina un documentos de los datos suplementarios de tramite
    public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet"); 
    }
    
    public boolean eliminarDocumentoRegistro(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");
    }
    
    
    public boolean setDocumentoDatoSuplementarioExpediente(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("not supported yet");         
    }
    
    
    public boolean eliminarTodosDocumentosDatosSuplementariosTramite(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("not supported yet");
    }
    
     public Documento getDocumentoRegistroConsulta(Documento doc,String origen) throws AlmacenDocumentoTramitacionException{
        throw new UnsupportedOperationException("not supported yet");
        
    }
     
     
     
     /**
     * Permite almacenar en base de datos un doucmento externo asociado a una notificación electrónica que se ç
     * envía al finalizar un trámite
     * @param doc: Objeto de la clase DocumentoBBDD
     * @return True si se ha dado de alta el doucmento y false en caso contrario
     * @throws AlmacenDocumentoTramitacionException si se produce algún error al dar de alta el documento
     */
    public boolean setDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("not supported yet");
    }

    
    
    public boolean eliminarDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("not supported yet");
    }
    
    
    public String getFirmaDocumentoExternoNotificacion(Documento doc,Connection con) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");
    }
    
    public boolean setFirmaDocumentoExternoNotificacion(Documento doc) throws AlmacenDocumentoTramitacionException{
         throw new UnsupportedOperationException("Not supported yet");
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
