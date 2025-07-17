package es.altia.agora.business.sge.plugin.documentos.sharepoint.ws.nextret;


import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.plugin.documentos.exception.TechnicalDocumentoException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.technical.ConstantesDatos;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.pozuelodealarcon.ListasLocator;
import org.pozuelodealarcon.ListasSoap;
import org.xml.sax.InputSource;

/**
 * Clase que utiliza el cliente del WS de NextRet para poder dar de manejar los documentos en el gestor documental SharePoint para la instalación de Pozuelo
 * @author oscar.rodriguez
 */
public class ClienteWSNextRetSharepoint {

    private Logger log = Logger.getLogger(ClienteWSNextRetSharepoint.class);
    private final String FICHERO_CONFIGURACION        = "documentos";
    
    /**
     * Da de alta un documento en el gestor documental SharePoint a través del WS de NextRet
     * @param doc: Documento
     * @return boolean
     */
    public boolean setDocumento(Documento doc) throws TechnicalDocumentoException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        // Conexión con el servicio web
        ListasSoap ws = null;
        try{

           ListasLocator locator = new ListasLocator();
           log.debug("==========================> " + this.getClass().getName() + ".setDocumento urlGestor: " +docGestor.getUrlGestor());
           ws = locator.getListasSoap12(new URL(docGestor.getUrlGestor()));
           if(ws==null){
               log.debug("Imposible realizar conexión con el servicio web de NextRet para el plugin de documentos");
           }
           else
               log.debug("REALIZADA CONEXIÓN CON EL WS DE NEXTRET");

        }catch(MalformedURLException e){
            log.debug(" =========> ERROR: LA URL DEL SERVICIO ESTÁ MAL FORMADA");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }catch(ServiceException e){
            log.debug(" =========> ERROR: NO HAY CONEXIÓN CON EL SERVICIO WEB");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }
        
        try{
            if(ws!=null){

                byte[] contenido = docGestor.getFichero();

                ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
                // SE RECUPERA EL NOMBRE DEL PLUGIN DEL FICHERO DE CONFIGURACIÓN
                String nombrePlugin = bundle.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento nombrePlugin: " + nombrePlugin);
                // SE RECUPERA EL CÓDIGO DE ÁREA DEL PROCEDIMIENTO DE LA BBDD
                String codArea = DefinicionProcedimientosManager.getInstance().getCodigoAreaProcedimiento(docGestor.getCodProcedimiento(), docGestor.getParams());
                log.debug("==========================> " + this.getClass().getName() + ".setDocumento codArea: " + codArea);

                // SE RECUPERA EL DOMINIO DEL USUARIO DE SHAREPOINT
                String aux = ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin + ConstantesDatos.BARRA
                                +  ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_DOMINIO;

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento propiedad dominio es: " + aux);
                String dominio = bundle.getString(aux);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento dominio: " + dominio);

                // Se recupera la definición de los metadatos del expediente
                String metadatos = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_METADATOS_EXPEDIENTE);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento metadatos: " + metadatos);

                // Métodos de DocumentoGestor que se utilizan para asignar los valores a los metados recuperados anteriormente
                String metodos = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento metodos: " + metodos);


                // Métodos de DocumentoGestor que se utilizan para asignar los valores a los metados recuperados anteriormente
                String metodosRelacion = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS_RELACION);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento metodosRelacion: " + metodosRelacion);

              

                /*************************************/

                // Se recupera la url de destino de la serie documental correspondiente al área del procedimiento para el alta de un documento
                String urlSerieDocumental = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL);
  
                String listaUrlSerieDocumental = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL_LISTA);

                String nombreListaAlojamientoExpediente =bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_LISTA);
                
                /*************************************/


                log.debug("==========================> " + this.getClass().getName() + ".setDocumento urlSerieDocumental: " + urlSerieDocumental);
                log.debug("==========================> " + this.getClass().getName() + ".setDocumento listaUrlSerieDocumental: " + listaUrlSerieDocumental);
                log.debug("==========================> " + this.getClass().getName() + ".setDocumento nombreListaAlojamientoExpediente: " + nombreListaAlojamientoExpediente);

                
                String tipoContenidoExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_TIPO_CONTENIDO_EXPEDIENTE);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento tipoContenidoExpediente: " + tipoContenidoExpediente);
                String valorTipoContenidoExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_VALOR_CONTENIDO_EXPEDIENTE);

                log.debug("==========================> " + this.getClass().getName() + ".setDocumento valorTipoContenidoExpediente: " + valorTipoContenidoExpediente);

                String codErrorExitoAltaExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXITO);


                log.debug("==========================> " + this.getClass().getName() + ".setDocumento codErrorExitoAltaExpediente: " + codErrorExitoAltaExpediente);
                String codErrorExisteExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXISTE);


                

                if(!docGestor.isDocRelacion())
                    docGestor.setNumeroExpediente(this.tratarNumeroExpediente(docGestor.getNumeroExpediente()));
                else{
                    docGestor.setNumeroExpediente(this.tratarNumeroRelacion(docGestor.getNumeroRelacion()));
                    docGestor.setNumeroRelacion(this.tratarNumeroRelacion(docGestor.getNumeroRelacion()));
                }

                log.debug("@@@@@@@@@ setDocumento es de relacion: " + docGestor.isDocRelacion());
                log.debug("@@@@@@@@@ setDocumento getNumeroExpediente: " + docGestor.getNumeroExpediente());
                log.debug("@@@@@@@@@ setDocumento getNumeroRelacion: " + docGestor.getNumeroRelacion());

                // Variable que contiene la url de la serie documental en la que se alojarán los expedientes
                String urlSerieDocumentalAltaExpediente = urlSerieDocumental;
                // Variable que contiene la url completa hasta llegar al documento alojado dentro de un expediente
                String urlSerieDocumentalAltaDocumento = null;


                log.debug("==========================> " + this.getClass().getName() + ".setDocumento codErrorExisteExpediente: " + codErrorExisteExpediente);

                log.debug("==================> setDocumento <===================");
                log.debug("@@@@@@@@@ url serie documental alta de expediente: " + urlSerieDocumentalAltaExpediente);
                log.debug("@@@@@@@@@ cod error exito operacion alta expediente correcta: " + codErrorExitoAltaExpediente);
                log.debug("@@@@@@@@@ cod error existe expediente operacion alta expediente correcta: " + codErrorExisteExpediente);
                log.debug(" @@@@@@@@@ setDocumento nombrePlugin " + nombrePlugin + " <> codArea procedimiento: " + codArea + " <> dominio: " + dominio);

                String[] listaMetadatos     = metadatos.split(ConstantesDatos.DOT_COMMA);
                
                String[] listaMetodos        = null;
                if(!docGestor.isDocRelacion()){
                    listaMetodos        = metodos.split(ConstantesDatos.DOT_COMMA);
                }else{
                    listaMetodos        = metodosRelacion.split(ConstantesDatos.DOT_COMMA);
                }

                String[] nombresMetadatosFijos = tipoContenidoExpediente.split(ConstantesDatos.COMMA);                
                log.debug("@@@@@@@@@@@ nombresMetadatosFijos : " + nombresMetadatosFijos.length);

                String[] valoresMetadatosFijos   = null;
                if(!docGestor.isDocRelacion()){
                    valoresMetadatosFijos   = valorTipoContenidoExpediente.split(ConstantesDatos.COMMA);
                }else
                    valoresMetadatosFijos   = valorTipoContenidoExpediente.split(ConstantesDatos.COMMA);

                // Se obtienen los arrays con los nombres de los metadatos y sus valores que se van a pasar al web service.                
                String[] nombreMetadatos = this.getNombresMetadatos(listaMetadatos, nombresMetadatosFijos);
                String[] valoresMetadatos = this.getValoresMetadatosVariables(docGestor, listaMetadatos, listaMetodos,nombresMetadatosFijos,valoresMetadatosFijos);



                log.debug("@@@@@@@@@ setDocumento parametros de createItemCredential <========");
                log.debug("@@@@@@@@@ setDocumento createItemCredential  urlSerieDocumentalAltaExpediente: " + urlSerieDocumentalAltaExpediente);
                log.debug(" @@@@@@@@@ setDocumento createItemCredential  nombreListaAlojamientoExpediente: " + nombreListaAlojamientoExpediente);
                log.debug(" @@@@@@@@@ setDocumento createItemCredential  nombreListaAlojamientoExpediente: " + nombreListaAlojamientoExpediente);
                log.debug(" @@@@@@@@@ setDocumento  MOSTRANDO LOS METADATOS ");
                log.debug("@@@@@@@@@ NUM METADATOS: " + nombreMetadatos.length);
                for(int z=0;z<nombreMetadatos.length;z++){
                    log.debug("@@@@@@@@@ nombreMetadatos[i]: " + nombreMetadatos[z]);
                }
                log.debug("");
                log.debug(" @@@@@@@@@ setDocumento  MOSTRANDO LOS VALORES DE LOS METADATOS ");
                for(int z=0;z<valoresMetadatos.length;z++){
                    log.debug("@@@@@@@@@ valoresMetadatos[i]: " + valoresMetadatos[z]);
                }



                // Se intenta crear el expediente para poder alojar dentro del mismo el documento
                String salidaOpAltaExpediente = ws.createItemCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(), dominio, urlSerieDocumentalAltaExpediente,nombreListaAlojamientoExpediente,nombreMetadatos,valoresMetadatos,"");
                log.debug("@@@@@@@@@ setDocumento después de la operación createItemsCredentials salidaOpAltaExpediente: " + salidaOpAltaExpediente);

                if(salidaOpAltaExpediente!=null && !"".equalsIgnoreCase(salidaOpAltaExpediente)){
                  
                  // Si la operación de alta del expediente es correcta
                   log.debug("@@@@@@@@@ setDocumento antes de comprobar la salida de alta expediente");

                   log.debug(" @@@@@@@@@ codErrorExitoAltaExpediente: " + codErrorExitoAltaExpediente + ", codErrorExisteExpediente:  " + codErrorExisteExpediente);
                  boolean continuar = this.operacionAltaExpedienteCorrecta(salidaOpAltaExpediente, codErrorExitoAltaExpediente, codErrorExisteExpediente);
                  log.debug("@@@@@@@@@ setDocumento despues de comprobar la salida de alta expediente " + continuar);
                  if(continuar){
                      // Ahora se da de alta el documento dentro del expediente
                  
                     // Se concatena el nombre del expediente a la url de destino para poder dar de alta el documento
                     //urlSerieDocumentalAltaDocumento = urlSerieDocumentalAltaDocumento + docGestor.getNumeroExpediente() + "/" +  docGestor.getNombreFicheroCompleto();

                    if(!docGestor.isDocRelacion())
                        urlSerieDocumentalAltaDocumento = urlSerieDocumental + ConstantesDatos.BARRA + listaUrlSerieDocumental +  ConstantesDatos.BARRA + docGestor.getNumeroExpediente() +
                                 ConstantesDatos.BARRA + docGestor.getNombreFicheroCompleto();
                    else
                        urlSerieDocumentalAltaDocumento = urlSerieDocumental + ConstantesDatos.BARRA + listaUrlSerieDocumental +  ConstantesDatos.BARRA + docGestor.getNumeroRelacion() +
                                 ConstantesDatos.BARRA + docGestor.getNombreFicheroCompleto();

                    
                    
                    
                    log.debug("@@@@@@@@@ urlSerieDocumentalAltaDocumento contenido.length: " + contenido.length);
                    log.debug(" @@@@@@@@@ setDocumento parametros de createDocumentCredentials <========");
                    log.debug(" @@@@@@@@@ setDocumento createDocumentCredentials  urlSerieDocumentalAltaDocumento: " + urlSerieDocumentalAltaDocumento);
                    log.debug(" @@@@@@@@@ setDocumento createDocumentCredentials  contenido.length: " + contenido.length);

                      String salidaAltaDocumento = ws.createDocumentCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(), dominio, urlSerieDocumentalAltaDocumento, contenido);
                      log.debug("@@@@@@@@@ salidaAltaDocumento: " + salidaAltaDocumento);
                      if(salidaAltaDocumento!=null && salidaAltaDocumento.length()>0 && "0".equals(salidaAltaDocumento)){
                            exito = true;
                      }

                  }//if

                }//if

               }else{
                   log.debug(" ****************** No hay conexión con el WS de Listas  ****************** ");
               }

        }catch(MissingResourceException e){
            log.debug(" ************************* Error al recuperar algún parámetro del fichero de configuración *********************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(13,"Error al recuperar los parámetros de configuración para la operación de alta de un documento" +  e.getMessage());
        }
        catch(RemoteException e){
            log.error(" ************************* ERROR AL CREAR LA CARPETA CON EL EXPEDIENTE ************************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }catch(IOException e){
            log.error(" ********************** ERROR DURANTE LA LECTURA DEL FICHERO QUE SE SUBE AL GESTOR ******************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }
        return exito;
    }



 /**
     * Modifica un determinado documento, para ello se elimina y se vuelve a dar de alta
     * @param doc: Documento
     * @return boolean
     */
    public boolean modificarDocumento(Documento doc) throws TechnicalDocumentoException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        // Conexión con el servicio web
        ListasSoap ws = null;
        try{
            log.debug("ClienteWSNextRetSharepoint . MODIFICARDOCUMENTO");
           ListasLocator locator = new ListasLocator();
           log.debug("=========> " + this.getClass().getName() + ".modificarDocumento urlGestor: " +docGestor.getUrlGestor());
           ws = locator.getListasSoap12(new URL(docGestor.getUrlGestor()));
           if(ws==null){
               log.debug("Imposible realizar conexión con el servicio web de NextRet para el plugin de documentos");
           }
           else
               log.debug("REALIZADA CONEXIÓN CON EL WS DE NEXTRET");

        }catch(MalformedURLException e){
            log.debug(" =========> ERROR: LA URL DEL SERVICIO ESTÁ MAL FORMADA");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al modificar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }catch(ServiceException e){
            log.debug(" =========> ERROR: NO HAY CONEXIÓN CON EL SERVICIO WEB");
            e.printStackTrace();
            throw new TechnicalDocumentoException(6,"Error al modificar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }

        try{
            if(ws!=null){

                byte[] contenido = docGestor.getFichero();

                ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
                // SE RECUPERA EL NOMBRE DEL PLUGIN DEL FICHERO DE CONFIGURACIÓN
                String nombrePlugin = bundle.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento nombrePlugin: " + nombrePlugin);
                // SE RECUPERA EL CÓDIGO DE ÁREA DEL PROCEDIMIENTO DE LA BBDD
                String codArea = DefinicionProcedimientosManager.getInstance().getCodigoAreaProcedimiento(docGestor.getCodProcedimiento(), docGestor.getParams());
                log.debug("========> " + this.getClass().getName() + ".modificarDocumento codArea: " + codArea);

                // SE RECUPERA EL DOMINIO DEL USUARIO DE SHAREPOINT
                String aux = ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin + ConstantesDatos.BARRA
                                +  ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_DOMINIO;

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento propiedad dominio es: " + aux);
                String dominio = bundle.getString(aux);

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento dominio: " + dominio);

                // Se recupera la definición de los metadatos del expediente
                String metadatos = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_METADATOS_EXPEDIENTE);

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento metadatos: " + metadatos);

                // Métodos de DocumentoGestor que se utilizan para asignar los valores a los metados recuperados anteriormente
                String metodos = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS);

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento metodos: " + metodos);


                // Métodos de DocumentoGestor que se utilizan para asignar los valores a los metados recuperados anteriormente
                String metodosRelacion = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS_RELACION);

                log.debug("========> " + this.getClass().getName() + ".modificarDocumento metodosRelacion: " + metodosRelacion);

                /*************************************/

                // Se recupera la url de destino de la serie documental correspondiente al área del procedimiento para el alta de un documento
                String urlSerieDocumental = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL);

                String listaUrlSerieDocumental = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL_LISTA);

                String nombreListaAlojamientoExpediente =bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_LISTA);

                String urlRelativaEliminarDocumento =bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_URL_ELIMINAR_DOCUMENTO);

                /*************************************/

                log.debug("=====> modificarDocumento urlSerieDocumental: " + urlSerieDocumental);
                log.debug("=====> modificarDocumento listaUrlSerieDocumental: " + listaUrlSerieDocumental);
                log.debug("=====> modificarDocumento nombreListaAlojamientoExpediente: " + nombreListaAlojamientoExpediente);
                log.debug("=====> modificarDocumento urlRelativaEliminarDocumento: " + urlRelativaEliminarDocumento);

                String tipoContenidoExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_TIPO_CONTENIDO_EXPEDIENTE);

                log.debug("=====> modificarDocumento tipoContenidoExpediente: " + tipoContenidoExpediente);
                String valorTipoContenidoExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_VALOR_CONTENIDO_EXPEDIENTE);
                log.debug("=====> modificarDocumento valorTipoContenidoExpediente: " + valorTipoContenidoExpediente);

                String codErrorExitoAltaExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXITO);


                log.debug("=====> modificarDocumento codErrorExitoAltaExpediente: " + codErrorExitoAltaExpediente);
                String codErrorExisteExpediente = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXISTE);

                if(!docGestor.isDocRelacion()){
                    docGestor.setNumeroExpediente(this.tratarNumeroExpediente(docGestor.getNumeroExpediente(),ConstantesDatos.BARRA,ConstantesDatos.GUION));
                }else{
                    docGestor.setNumeroExpediente(this.tratarNumeroExpediente(docGestor.getNumeroRelacion(),ConstantesDatos.BARRA,ConstantesDatos.GUION));
                    docGestor.setNumeroRelacion(this.tratarNumeroExpediente(docGestor.getNumeroRelacion(),ConstantesDatos.BARRA,ConstantesDatos.GUION));
                }
                
                /**** SE BUSCA EL DOCUMENTO PARA RECUPERAR SU ID ***/
                String[] atributos = new String[3];

                log.debug(" ************* eliminarDocumento parametros antes de la llamada getItemsCredential **********************");
                log.debug(" ************* getItemsCredentials getUsuarioGestor: " + docGestor.getUsuarioGestor());
                log.debug(" ************* getItemsCredentials getPaswordGestor: " + docGestor.getPaswordGestor());
                log.debug(" ************* getItemsCredentials dominio: " + dominio);
                log.debug(" ************* getItemsCredentials urlSerieDocumental: " + urlSerieDocumental);
                log.debug(" ************* getItemsCredentials urlSerieDocumental: " + nombreListaAlojamientoExpediente);
                log.debug(" ************* getItemsCredentials expediente: " + docGestor.getNumeroExpediente());

                String salidaGetItems = ws.getItemsCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(),dominio,urlSerieDocumental,nombreListaAlojamientoExpediente,atributos,docGestor.getNumeroExpediente());
                log.debug("=================> eliminarDocumento(), resultado operacion getItems: " + salidaGetItems);

                 // Se procede a comprobar si entre el contenido del expediente, se encuentra el documento a buscar
                String nombreFicheroExtension = docGestor.getNombreFicheroCompleto();
                String nombreSinExtension = nombreFicheroExtension.substring(0, nombreFicheroExtension.lastIndexOf("."));
                if(nombreSinExtension!=null){
                   log.debug("************ buscando idDocumento para documento nombreSinExtension: " + nombreSinExtension);
                   String idDocumento = this.getIdDocumento(salidaGetItems,nombreSinExtension);
                   log.debug("************ idDocumento: " + idDocumento);
                   if(idDocumento!=null){
                       log.debug(" El documento " +nombreSinExtension + " existe en el expediente " + docGestor.getNumeroExpediente() + "y su ID es: " + idDocumento);

                       /** SE PROCEDE A ELIMINAR EL DOCUMENTO PARA, A CONTINUACIÓN, DARLO DE ALTA DE NUEVO **/

                       // Atención con la tilde de la lista Expedientes de contratacin
                       String fileRef = urlRelativaEliminarDocumento + ConstantesDatos.BARRA +  docGestor.getNumeroExpediente() + ConstantesDatos.BARRA + docGestor.getNombreFicheroCompleto();

                        log.debug(" ************* eliminarDocumento parametros antes de la llamada deleteItemCredentials **********************");
                        log.debug(" ************* deleteItemCredentials getUsuarioGestor: " + docGestor.getUsuarioGestor());
                        log.debug(" ************* deleteItemCredentials getPaswordGestor: " + docGestor.getPaswordGestor());
                        log.debug(" ************* deleteItemCredentials dominio: " + dominio);
                        log.debug(" ************* deleteItemCredentials urlSerieDocumental: " + urlSerieDocumental);
                        log.debug(" ************* deleteItemCredentials nombreListaAlojamientoExpediente: " + nombreListaAlojamientoExpediente);
                        log.debug(" ************* deleteItemCredentials fileRef: " + fileRef);
                        log.debug(" ************* deleteItemCredentials idDocumento: " + idDocumento);
                        log.debug(" ************* deleteItemCredentials expediente: " + docGestor.getNumeroExpediente());

                       // El nombre de la lista en las llamadas al servicio web va con tilde si la tiene. En las url de destino va sin ella
                       String salidaWSEliminar = ws.deleteItemCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(),dominio,urlSerieDocumental,nombreListaAlojamientoExpediente, fileRef, idDocumento,"");
                       log.debug("salidaWSEliminar: " + salidaWSEliminar);

                       if(this.salidaOperacionWSEliminacion(salidaWSEliminar)){
                            // Se ha eliminado el documento antiguo, entonces ahora se procede a darlo de alta el nuevo porque su contenido o su nombre puede haber sido modificado
                            log.debug(" ======= el documento " + fileRef + " ha sido eliminado");
                            String urlSerieDocumentalAltaDocumento = urlSerieDocumental + ConstantesDatos.BARRA + listaUrlSerieDocumental +  ConstantesDatos.BARRA + docGestor.getNumeroExpediente() +
                            ConstantesDatos.BARRA + docGestor.getNuevoNombreFicheroCompleto();

                            log.debug("======= el viejo documento ha sido eliminado");
                            log.debug(" ====== urlDestino para el alta del documento: " + urlSerieDocumentalAltaDocumento);
                            log.debug(" ====== contenido del documento length: " + contenido.length);

                            log.debug("");
                            log.debug(" ************* eliminarDocumento parametros antes de la llamada createDocumentCredentials **********************");
                            log.debug(" ************* createDocumentCredentials getUsuarioGestor: " + docGestor.getUsuarioGestor());
                            log.debug(" ************* createDocumentCredentials getPaswordGestor: " + docGestor.getPaswordGestor());
                            log.debug(" ************* createDocumentCredentials dominio: " + dominio);
                            log.debug(" ************* createDocumentCredentials urlSerieDocumentalAltaDocumento: " + urlSerieDocumentalAltaDocumento);
                            log.debug(" ************* createDocumentCredentials contenido.length: " + contenido.length);
                            
                           String salidaAltaDocumento = ws.createDocumentCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(), dominio, urlSerieDocumentalAltaDocumento, contenido);
                           log.debug("===========> salidaAltaDocumento: " + salidaAltaDocumento);
                           if(salidaAltaDocumento!=null && salidaAltaDocumento.length()>0 && "0".equals(salidaAltaDocumento)){
                                exito = true;
                           }
                       }
                       else
                           exito = false;
                   }
                }

               }else{
                   log.debug(" ****************** No hay conexión con el WS de Listas  ****************** ");
               }

        }catch(MissingResourceException e){
            log.debug(" ************************* Error al recuperar algún parámetro del fichero de configuración *********************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(6,"Error al recuperar los parámetros de configuración para la operación de modificar un documento de tramitación " +  e.getMessage());
        }
        catch(RemoteException e){
            log.error(" ************************* ERROR AL CREAR LA CARPETA CON EL EXPEDIENTE ************************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(6,"Error al modificar el documento " + docGestor.getNombreDocumento() + " en el gestor documental SharePoint a través del WS de NextRet");
        }catch(IOException e){
            log.error(" ********************** ERROR DURANTE LA LECTURA DEL FICHERO QUE SE SUBE AL GESTOR ******************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(6,"Error al modificar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }
        return exito;
    }



    private void mostrar(String[] lista){
        log.debug(" ==========> Mostrando el contenido de " + lista + " <================");
        for(int i=0;i<lista.length;i++){
            log.debug(" ******* pos  " + i + " y su contenido es:" + lista[i]);
        }
        log.debug(" ==========> Lista visualizada  <================");
    }

    /**
     * Comprueba si la salida devuelta por la operación de alta de expediente es correcta
     * @param salida: String con el XML devuelto por la operación de alta de un expediente
     * @param codErrorExito: Código de error que indica que se ha creado el expediente
     * @param codErrorExiste: Código del error que indica que existe el expediente
     * @return
     */
    private boolean operacionAltaExpedienteCorrecta(String salida,String codErrorExito,String codErrorExiste){
        boolean exito = false;
        try{
            SAXBuilder sax = new SAXBuilder(false);
            Reader reader = new InputStreamReader(new ByteArrayInputStream(salida.getBytes()),"UTF-8");
            InputSource is = new InputSource(reader);

            org.jdom.Document doc = sax.build(is);

            log.debug(" ===========> operacionAltaExpedienteCorrecta PARAMETROS");
            log.debug(" ===========> operacionAltaExpedienteCorrecta salida: " +salida);
            log.debug(" ===========> operacionAltaExpedienteCorrecta codErrorExito: " +codErrorExito + " y codErrorExiste:  " + codErrorExiste);
            
            if(doc!=null){
                Element raiz = doc.getRootElement();
                List hijos = raiz.getChildren();
                
                for(int i=0;i<hijos.size();i++){
                    Element hijo = (Element)hijos.get(i);
                    log.debug(" ===========> operacionAltaExpedienteCorrecta nombre: " + hijo.getName() + " y valor: " + hijo.getValue());
                    if(hijo.getName().equals(ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_RESPUESTA_WS_ERRORCODE)){                        
                        // Si el código de error es el correcto o bien ya existe el expediente => Entonces se retorna un true porque es lo que se esperaba
                        if(hijo.getValue().trim().equals(codErrorExito.trim()) || hijo.getValue().trim().equals(codErrorExiste.trim())){
                            log.debug(" ===========> operacionAltaExpedienteCorrecta hay coincidencia en el código de error");
                            exito  = true;
                        }else
                            log.debug(" ===========> operacionAltaExpedienteCorrecta no hay coincidencia en el código de error");
                    }
                } // for
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return exito;
    }


    /**
     * Trata el numero del expediente para poder crear un expediente en el gestor documental
     * @param numExpediente: Número de expediente
     * @return Número de expediente tratado
     */
    private String tratarNumeroExpediente(String numExpediente){
       String salida = "";
        StringBuffer sb = new StringBuffer();


        if(numExpediente!=null && numExpediente.length()>0){
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);

            for(int i=0;datos!=null && i<datos.length;i++){
                sb.append(datos[i]);
                if(datos.length-i>1)
                    sb.append(ConstantesDatos.GUION);
            }
        }

        return sb.toString();
    }


   /**
     * Trata el numero de la relación para poder crear una relación en el gestor documental
     * @param numExpediente: Número de expediente
     * @return Número de expediente tratado
     */
    private String tratarNumeroRelacion(String numExpediente){
       String salida = "";
        StringBuffer sb = new StringBuffer();


        if(numExpediente!=null && numExpediente.length()>0){
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);

            for(int i=0;datos!=null && i<datos.length;i++){
                sb.append(datos[i]);
                if(datos.length-i>1)
                    sb.append(ConstantesDatos.GUION);
            }
        }

        log.debug(" Número de la relación tratado : " + sb.toString());
        return sb.toString();
    }




    public String tratarNumeroExpediente(String numExpediente,String separadorOriginal,String separadorFinal){
       String salida = "";
        StringBuffer sb = new StringBuffer();


        if(numExpediente!=null && numExpediente.length()>0){
            String[] datos = numExpediente.split(separadorOriginal.trim());

            for(int i=0;datos!=null && i<datos.length;i++){
                sb.append(datos[i]);
                if(datos.length-i>1)
                    sb.append(separadorFinal.trim());
            }
        }

        return sb.toString();
    }




  /**
     * Devuelve un array de String con los valores de los metadatos que se obtienen del objeto DocumentoGestor
     * @param doc: Objeto de la clase DocumentoGestor
     * @param metadatos: Array de String con los nombres de los metadatos variables a pasar a las operaciones del web service que lo requieran
     * @param metodos: Nombre de los métodos
     * @param metadatosFijos: Array con los nombres de los metadatos fijos de los cuales ya se conocen los valores.
     * @param valoresMetadatosFijos: Array con los valores de los metadatos fijos 
     * @return Array de String con los valores o null si no se puede recuperar dichos valores
     */
     private String[] getValoresMetadatosVariables(DocumentoGestor doc,String[] metadatos,String[] metodos,String[] metadatosFijos,String[] valoresMetadatosFijos) throws TechnicalDocumentoException{
        String[] valores = null;

        int lenMetadatosVariables = 0;
        int lenMetadatosFijos = 0;

        try{

            if(metodos!=null && metadatos!=null && metodos.length==metadatos.length)
                lenMetadatosVariables = metodos.length;

            if(metadatosFijos!=null && valoresMetadatosFijos!=null && metadatosFijos.length==valoresMetadatosFijos.length)
                lenMetadatosFijos = metadatosFijos.length;

            log.debug("==========> getValoresMetadatosVariables. lenMetadatosFijos: " + lenMetadatosFijos + ", lenMetadatosVariables: " + lenMetadatosVariables);
            valores = new String[lenMetadatosFijos + lenMetadatosVariables];

            if(metodos!=null && metadatos!=null && metodos.length==metadatos.length){
        
                // Se obtienen los valores de los metadatos variables y se rellena el array valores
                int i=0;
                for(i=0;i<metadatos.length;i++){
                    String metadato = metadatos[i];
                    String metodo   = metodos[i];

                    String valor = getValorMetodo(doc,metodo);
                    log.debug("metadato: " + metadato + ", metodo: " + metodo + " tiene como valor: " + valor);
                    if(valor!=null && valor.length()>0)
                        valores[i] = valor;

                }// for

               log.debug("==========> getValoresMetadatosVariables. MOSTRANDO LOS VALORES DE LOS METADATOS VARIABLES");
               log.debug("El valor de i es: " + i + " y la longitud del array valores es: " + valores.length);
               this.mostrar(valores);

                // Se obtienen los valores de los metadatos fijos y se rellena el array valores
                for(int j=0;metadatosFijos!=null && j<metadatosFijos.length;j++){
                    log.debug("Añadiendo metadato fijo : " + valoresMetadatosFijos[j] + " y valor de i: " + i);
                    valores[i] = valoresMetadatosFijos[j];
                    i++;
                }

               log.debug("==========> getValoresMetadatosVariables MOSTRANDO LOS VALORES DE LOS METADATOS FIJOS " + valores);
               this.mostrar(valores);

            }//if
        }catch(Exception e){
            e.printStackTrace();
            throw new TechnicalDocumentoException(13,"Error al recuperar parámetros de conexión");
        }
        return valores;
    }



  /**
     * Devuelve un array de String con los valores de los metadatos que se obtienen del objeto DocumentoGestor
     * @param doc: Objeto de la clase DocumentoGestor
     * @param metadatos: Array de String con los nombres de los metadatos variables a pasar a las operaciones del web service que lo requieran
     * @param metodos: Nombre de los métodos
     * @param metadatosFijos: Array con los nombres de los metadatos fijos de los cuales ya se conocen los valores.
     * @param valoresMetadatosFijos: Array con los valores de los metadatos fijos
     * @return Array de String con los valores o null si no se puede recuperar dichos valores
     */
     private String[] getNombresMetadatos(String[] metadatosVariables,String[] metadatosFijos){
        String[] datos = null;
        int lenMetadatosVariables = metadatosVariables.length;
        int lenMetadatosFijos = metadatosFijos.length;

        log.debug("===========> getNombresMetadatos metadata fijo len : " + metadatosFijos.length + ", metadatosVariables  len " + metadatosVariables.length);
        datos = new String[lenMetadatosFijos + lenMetadatosVariables];        
        // Se obtienen los valores de los metadatos variables y se rellena el array valores
        int i=0;
        for(i=0;i<metadatosVariables.length;i++){
            datos[i] = metadatosVariables[i];
        }// for

        log.debug("============>getNombresMetadatos mostrando despues de añadir metadatos fijos");
        this.mostrar(datos);

        // Se obtienen los valores de los metadatos fijos y se rellena el array valores
        for(int j=0;metadatosFijos!=null && j<metadatosFijos.length;j++){
            datos[i] = metadatosFijos[j];
            i++;
        }

        log.debug("============>getNombresMetadatos mostrando despues de añadir metadatos variables");
        this.mostrar(datos);

        return datos;
    }



     /**
     * Recupera el valor de un método de un objeto determinado
     * @param objeto: Objeto a consulta de la clase ExpedienteArtemisVO
     * @param nombreMetodo: Nombre del método del objeto del que se quiere obtener su valor
     * @return String con el valor del método
     */
   private String getValorMetodo(Documento objeto,String nombreMetodo){
        String valor ="";

        try{

            log.debug("======> getValorMetodos: " + nombreMetodo);
            Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

            Class<Class> parametros = null;
            Object[] objetos = null;
            Method metodo = clase.getMethod(nombreMetodo,null);
            valor = (String)metodo.invoke(objeto,null);
            log.debug("======> getValorMetodos valor: " + valor);

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }


        return valor;
    }



      /**
     * Da de alta un documento en el gestor documental SharePoint a través del WS de NextRet
     * @param doc: Documento
     * @return boolean
     */
    public boolean eliminarDocumento(Documento doc) throws TechnicalDocumentoException{
        boolean exito = false;
        DocumentoGestor docGestor = (DocumentoGestor)doc;

        // Conexión con el servicio web
        ListasSoap ws = null;
        try{

           ListasLocator locator = new ListasLocator();
           log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento urlGestor: " +docGestor.getUrlGestor());
           ws = locator.getListasSoap12(new URL(docGestor.getUrlGestor()));
           if(ws==null){
               log.debug("Imposible realizar conexión con el servicio web de NextRet para el plugin de documentos");
           }
           else
               log.debug("REALIZADA CONEXIÓN CON EL WS DE NEXTRET");

        }catch(MalformedURLException e){
            log.debug(" =========> ERROR: LA URL DEL SERVICIO ESTÁ MAL FORMADA");
            e.printStackTrace();
            throw new TechnicalDocumentoException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental SharePoint a través del WS de NextRet");
        }catch(ServiceException e){
            log.debug(" =========> ERROR: NO HAY CONEXIÓN CON EL SERVICIO WEB");
            e.printStackTrace();
            throw new TechnicalDocumentoException(5,"Error al eliminar el documento de tramitación " + docGestor.getNombreDocumento() + " del gestor documental SharePoint a través del WS de NextRet");
        }

        try{
            if(ws!=null){

                ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
                // SE RECUPERA EL NOMBRE DEL PLUGIN DEL FICHERO DE CONFIGURACIÓN
                String nombrePlugin = bundle.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);

                log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento nombrePlugin: " + nombrePlugin);
                // SE RECUPERA EL CÓDIGO DE ÁREA DEL PROCEDIMIENTO DE LA BBDD
                String codArea = DefinicionProcedimientosManager.getInstance().getCodigoAreaProcedimiento(docGestor.getCodProcedimiento(), docGestor.getParams());
                log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento codArea: " + codArea);

                // SE RECUPERA EL DOMINIO DEL USUARIO DE SHAREPOINT
                String aux = ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin + ConstantesDatos.BARRA
                                +  ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_DOMINIO;

                log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento propiedad dominio es: " + aux);
                String dominio = bundle.getString(aux);

                log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento dominio: " + dominio);


                /*************************************/

                // Se recupera la url de destino de la serie documental correspondiente al área del procedimiento para el alta de un documento
                String urlSerieDocumental = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL);
                                
                String urlRelativaEliminarDocumento =bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_URL_ELIMINAR_DOCUMENTO);


                String listaExpedientes =bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_LISTA);

                /*************************************/

                log.debug("======> " + this.getClass().getName() + ".eliminarDocumento urlSerieDocumental: " + urlSerieDocumental);
                log.debug("======> " + this.getClass().getName() + ".eliminarDocumento urlRelativaEliminarDocumento: " + urlRelativaEliminarDocumento);
                log.debug("======> " + this.getClass().getName() + ".eliminarDocumento listaExpedientes: " + listaExpedientes);
              
                String codErrorOPEliminar = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + docGestor.getCodMunicipio() + ConstantesDatos.BARRA + nombrePlugin
                                +  ConstantesDatos.BARRA + codArea + ConstantesDatos.BARRA + ConstantesDatos.ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ELIMINARDOCUMENTO);

                docGestor.setNumeroExpediente(this.tratarNumeroExpediente(docGestor.getNumeroExpediente(),"/","-"));

                log.debug("==========================> " + this.getClass().getName() + ".eliminarDocumento codErrorOPEliminar: " + codErrorOPEliminar);

                String[] atributos = new String[3];

                String salidaGetItems = ws.getItemsCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(),dominio,urlSerieDocumental,listaExpedientes,atributos,docGestor.getNumeroExpediente());
                log.debug("=================> eliminarDocumento(), resultado operacion getItems: " + salidaGetItems);

                 // Se procede a comprobar si entre el contenido del expediente, se encuentra el documento a buscar
                String nombreFicheroExtension = docGestor.getNombreFicheroCompleto();
                String nombreSinExtension = nombreFicheroExtension.substring(0, nombreFicheroExtension.lastIndexOf("."));
                if(nombreSinExtension!=null){
                   log.debug("************ buscando idDocumento para documento nombreSinExtension: " + nombreSinExtension);
                   String idDocumento = this.getIdDocumento(salidaGetItems,nombreSinExtension);
                   log.debug("************ idDocumento: " + idDocumento);
                   if(idDocumento!=null){
                       log.debug(" El documento " +nombreSinExtension + " existe en el expediente " + docGestor.getNumeroExpediente() + "y su ID es: " + idDocumento);

                       // Atención con la tilde de la lista Expedientes de contratacin
                       String fileRef = urlRelativaEliminarDocumento + ConstantesDatos.BARRA +  docGestor.getNumeroExpediente() + ConstantesDatos.BARRA + docGestor.getNombreFicheroCompleto();

                       log.debug("Ruta relativa al documento a eliminar: " + fileRef);

                       // El nombre de la lista en las llamadas al servicio web va con tilde si la tiene. En las url de destino va sin ella
                       String salidaWSEliminar = ws.deleteItemCredentials(docGestor.getUsuarioGestor(),docGestor.getPaswordGestor(),dominio,urlSerieDocumental,listaExpedientes, fileRef, idDocumento,"");
                       log.debug("salidaWSEliminar: " + salidaWSEliminar);

                       if(this.salidaOperacionWSEliminacion(salidaWSEliminar))
                           exito = true;
                       else
                           exito = false;
                   }
                }

               }else{
                   log.debug(" ****************** No hay conexión con el WS de Listas  ****************** ");
               }

        }catch(MissingResourceException e){
            log.error(" ************************* Error al recuperar algún parámetro del fichero de configuración *********************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(5,"Error al recuperar los parámetros de configuración para la operación de eliminación de un documento" +  e.getMessage());
        }
        catch(RemoteException e){            
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }catch(IOException e){
            log.error(" ********************** ERROR DURANTE LA LECTURA DEL FICHERO QUE SE SUBE AL GESTOR ******************************");
            e.printStackTrace();
            throw new TechnicalDocumentoException(1,"Error al grabar el documento " + docGestor.getNombreDocumento() + "en el gestor documental SharePoint a través del WS de NextRet");
        }

        return exito;
    }



     public String getIdDocumento(String datos,String archivo){
          String salida = null;

          try{
            // Se lee el fichero xml
            SAXBuilder builder = new SAXBuilder();
            org.jdom.Document documento = builder.build(new StringReader(datos));

            org.jdom.Element root = documento.getRootElement();
            java.util.List hijosRaiz = root.getChildren();

            for (Iterator<Element> it = hijosRaiz.iterator(); it.hasNext();)
            {
                Element dato = (Element) it.next();

                if(dato.getName().equals("row")){
                    org.jdom.Attribute baseName = dato.getAttribute("ows_BaseName");

                    log.debug("Comparando elemento " + baseName.getValue() + " con nombre de archivo: " + archivo);
                    log.debug("Comparando elemento.length:  " + baseName.getValue().length() + " con longitud de archivo: " + archivo.length());
                    if(baseName!=null && baseName.getValue().trim().equals(archivo.trim())){
                        org.jdom.Attribute owsID = dato.getAttribute("ows_ID");
                        salida =owsID.getValue();
                        log.debug("Se ha encontrado " + salida);


                        org.jdom.Attribute serverUrl = dato.getAttribute("ows_ServerUrl");
                        org.jdom.Attribute encoded = dato.getAttribute("ows_EncodedAbsUrl");
                        org.jdom.Attribute ows_FileRef = dato.getAttribute("ows_FileRef");

                        log.debug(" ows_ServerUrl : " + serverUrl.getValue());
                        log.debug(" ows_EncodedAbsUrl : " + encoded.getValue());
                        log.debug(" ows_FileRef : " + ows_FileRef.getValue());
                        break;

                    }

                }// if

            }// for
        }
        catch(Exception e){
            e.printStackTrace();
        }

          return salida;
      }




        /**
       * Recupera el Id de un determinado documento
       * @param xml: XML con la salida de la operación getItemsCredentials del WS de NextRet
       * @param archivo: Nombre del archivo sin la extensión
       * @return El id del documento o null sino se ha podido recuperar
       *
      public String getIdDocumento(String datos,String archivo){
          String salida = null;

          try{           
            // Se lee el fichero xml
            SAXBuilder builder = new SAXBuilder();            
            org.jdom.Document documento = builder.build(new StringReader(datos));

            org.jdom.Element root = documento.getRootElement();
            java.util.List hijosRaiz = root.getChildren();

            for (Iterator<Element> it = hijosRaiz.iterator(); it.hasNext();)
            {
                Element dato = (Element) it.next();
                
                if(dato.getName().equals("row")){
                    org.jdom.Attribute baseName = dato.getAttribute("ows_BaseName");
                    log.debug(" ============== > comparando valor " + baseName.getValue() + " con nombre de archivo : " + archivo);
                    if(baseName!=null && baseName.getValue().trim().equals(archivo.trim())){
                        org.jdom.Attribute owsID = dato.getAttribute("ows_ID");
                        salida =owsID.getValue();
                    }

                }// if

            }// for
        }
        catch(Exception e){
            e.printStackTrace();
        }

          return salida;
      }
      */



      /**
      * Comprueba si la salida de la operación del ws de eliminación es correcta
      * @param xml: Salida devuelta por la operación de eliminar un item del WS de NextRet
      * @return Boolean
      */
     private boolean salidaOperacionWSEliminacion(String xml){
           boolean exito = false;

           try{
                // Se lee el fichero xml
                SAXBuilder builder = new SAXBuilder();
                //org.jdom.Document documento = builder.build(datos);
                org.jdom.Document documento = builder.build(new StringReader(xml));


                 if(documento!=null){
                    Element raiz = documento.getRootElement();
                    List hijos = raiz.getChildren();

                    for(int i=0;i<hijos.size();i++){
                        Element hijo = (Element)hijos.get(i);
                        log.debug(" ===========> salidaOperacionWSEliminacion nombre: " + hijo.getName() + " y valor: " + hijo.getValue());
                        if(hijo.getName().equals("ErrorCode")){
                            // Si el código de error es el correcto o bien ya existe el expediente => Entonces se retorna un true porque es lo que se esperaba
                            if(hijo.getValue().trim().equals("0x00000000")){
                                log.debug(" ===========> salidaOperacionWSEliminacion hay coincidencia en el código de error");
                                exito  = true;
                            }else
                                log.debug(" ===========> salidaOperacionWSEliminacion no hay coincidencia en el código de error");
                        }
                    } // for
                }
           }catch(Exception e){
               e.printStackTrace();
           }

            return exito;
     }



}