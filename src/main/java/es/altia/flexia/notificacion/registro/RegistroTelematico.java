
package es.altia.flexia.notificacion.registro;

import es.altia.flexia.notificacion.vo.ApunteRegistroVO;
import es.altia.flexia.notificacion.vo.DireccionRegistroVO;
import es.altia.flexia.notificacion.vo.TerceroRegistroVO;
import es.altia.merlin.licitacion.exceptions.SleException;
//import es.altia.telematicregistry.TelematicRegistry_PortType;
import es.altia.telematicregistry.TelematicRegistry_ServiceLocator;
import es.altia.telematicregistry.serialization.xml.objects.Address;
import es.altia.telematicregistry.serialization.xml.objects.DestinationApplicant;
import es.altia.telematicregistry.serialization.xml.objects.DestinationPartyList;
import es.altia.telematicregistry.serialization.xml.objects.EntryData;
import es.altia.telematicregistry.serialization.xml.objects.FullSignature;
import es.altia.telematicregistry.serialization.xml.objects.Notification;
import es.altia.telematicregistry.serialization.xml.objects.SenderAppInfo;
import es.altia.telematicregistry.serialization.xml.objects.SenderParty;
import es.altia.telematicregistry.serialization.xml.objects.SenderUnit;
import es.altia.telematicregistry.TelematicRegistry_PortType;
import es.altia.telematicregistry.serialization.xml.SetExitRegistryRequestXML;
import es.altia.telematicregistry.serialization.xml.SetExitRegistryResultXML;
import es.altia.telematicregistry.serialization.xml.objects.CreateExitRequest;
import es.altia.telematicregistry.serialization.xml.objects.CreateExitResult;
import es.altia.telematicregistry.serialization.xml.objects.DestinationUnit;
import es.altia.telematicregistry.serialization.xml.objects.DestinationUnit;
import es.altia.telematicregistry.serialization.xml.objects.DocumentList;
import es.altia.telematicregistry.serialization.xml.objects.DocumentUnit;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;

public class RegistroTelematico {

    private static RegistroTelematico instance = null;
    private Logger log = Logger.getLogger(RegistroTelematico.class);   
    private String urlWS = null;
   
    private RegistroTelematico(){
        try{
            if(urlWS==null || "".equals(urlWS)){
                
                ResourceBundle configNotif = ResourceBundle.getBundle("notificaciones");
                urlWS = configNotif.getString("URL_WS_TELEMATICO");                
            }
        }catch(Exception e){
            log.error("Error al obtener al leer la url del registro telemático del fichero de configuración de notificaciones");
        }
    }
    
    public static RegistroTelematico getInstance(){
        if(instance==null) instance = new RegistroTelematico();
        return instance;
    }

    /*
    private String crearMensajeExit(es.altia.flexia.notificacion.vo.ApunteRegistroVO apunte,String codOrganizacion) throws SleException {

        ResourceBundle config = ResourceBundle.getBundle("notificaciones");
        

		StringBuffer mensaje = new StringBuffer();
		mensaje.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		mensaje.append("<createExitRegistryRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"createExitRegistryRequest.xsd\">");
			mensaje.append("<entryData>");
				mensaje.append("<annotationType>EXIT</annotationType>"); // Tipo de anotación: Salida
				mensaje.append("<senderParty>");
					mensaje.append("<senderUnit>");
						mensaje.append("<entity>" + apunte.getEntidad() + "</entity>");
						mensaje.append("<organization>" + apunte.getCodOrganoOrigen() + "</organization>");
						mensaje.append("<department>" + apunte.getCodUnidadOrigen() + "</department>");
					mensaje.append("</senderUnit>");

				mensaje.append("</senderParty>");
				mensaje.append("<destinationPartyList>");


                    for(int i=0;apunte.getTerceros()!=null && i<apunte.getTerceros().size();i++){

                        TerceroRegistroVO tercero = apunte.getTerceros().get(i);
                        // El codigo postal tiene que ser el
                        mensaje.append("<destinationApplicant>");
                            mensaje.append("<name><![CDATA[" + tercero.getNombreTercero() + "]]></name>");
                            mensaje.append("<surname></surname>");
                            mensaje.append("<codeID>" + tercero.getNifTercero() + "</codeID>");
                            mensaje.append("<typeID>" + tercero.getTipoDocumento() + "</typeID>");
                            mensaje.append("<address>");
                                mensaje.append("<street><![CDATA[" + tercero.getDireccion().getDireccion() + "]]></street>");
                                mensaje.append("<city><![CDATA[" + tercero.getDireccion().getDescripcionMunicipio() + "]]></city>");
                                mensaje.append("<province>" + tercero.getDireccion().getCodigoProvincia() + "</province>");
                                mensaje.append("<country>" + tercero.getDireccion().getCodigoPais() + "</country>");
                                mensaje.append("<zipCode>" + tercero.getDireccion().getCodigoPostal() + "</zipCode>");
                                mensaje.append("<phoneNumber>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getTelfRemitente(), "NA")).append("</phoneNumber>");
                                mensaje.append("<faxNumber>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getFaxRemitente(), "NA")).append("</faxNumber>");
                                mensaje.append("<email>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getEmailRemitente(), "NA")).append("</email>");
                            mensaje.append("</address>");
                            mensaje.append("<notification>");
                                mensaje.append("<notificationType>TELEMATIC</notificationType>");
                                mensaje.append("<street><![CDATA[" + tercero.getDireccion().getDireccion() + "]]></street>");
                                mensaje.append("<city><![CDATA[" + tercero.getDireccion().getDescripcionMunicipio() + "]]></city>");
                                mensaje.append("<province>" + tercero.getDireccion().getCodigoProvincia() + "</province>");
                                mensaje.append("<country>" + tercero.getDireccion().getCodigoPais() + "</country>");
                                mensaje.append("<zipCode>" + tercero.getDireccion().getCodigoPostal() + "</zipCode>");
                                mensaje.append("<phoneNumber>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getTelfRemitente(), "NA")).append("</phoneNumber>");
                                mensaje.append("<faxNumber>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getFaxRemitente(), "NA")).append("</faxNumber>");
                                mensaje.append("<email>").append(BasicTypesOperations.safeToString(tercero.getDireccion().getEmailRemitente(), "NA")).append("</email>");
                            mensaje.append("</notification>");
                        mensaje.append("</destinationApplicant>");
                    }// for
                    

				mensaje.append("</destinationPartyList>");
				mensaje.append("<senderAppInfo>");
					mensaje.append("<appCode>" + codigoSLE + "</appCode>");
					mensaje.append("<procedureCode>" + apunte.getCodProcedimiento() + "</procedureCode>");
					mensaje.append("<folderID>" + apunte.getNumExpediente() + "</folderID>");
				mensaje.append("</senderAppInfo>");
				mensaje.append("<subject><![CDATA[" + apunte.getAsunto() + "]]></subject>");
				mensaje.append("<body><![CDATA[" + apunte.getTexto() + "]]></body>");
				mensaje.append("<documentList>");
                /*
				DocumentoVO[] doc = apunte.getDocumentos();
				if (doc != null) {
					for (int i=0; i<apunte.getDocumentos().length; i++) {
						try {
							mensaje.append("<document>");
                            mensaje.append("<documentName>").append(doc[i].getNombre()).append("</documentName>");
                            mensaje.append("<documentHash>").append(getHash(doc[i].getDocumento())).append("</documentHash>");
                            mensaje.append("<documentContent>").append(getDocumentContents(doc[i].getDocumento())).append("</documentContent>");
							mensaje.append("</document>");
						} catch (NoSuchAlgorithmException e) {
							log.error("Error al obtener el hash", e);
							throw new SleException("Error al obtener el hash del documento");
						}
					}
				}
				mensaje.append("</documentList>");
			mensaje.append("</entryData>");

			String firma;
            PluginPortafirmas pluginFirma = PluginPortafirmasFactoria.getImplClass(codOrganizacion);

			//PlataformaPKI servicioFirma = FactoriaPlataformaPKI.getPlataformaPKI();
			try {
				//firma = servicioFirma.firmaServidor(mensaje.toString());
                firma = pluginFirma.firmaServidor(mensaje.toString().getBytes());

			} catch (java.lang.Exception e) {
				log.error("Error al obtener la firma", e);
				throw new SleException("Error al obtener la firma");
			}

			mensaje.append("<fullSignature>");
				mensaje.append("<signature>" + firma + "</signature>");
				mensaje.append("<timestamp>" + "NA" + "</timestamp>");
				mensaje.append("<ocsp>" + "NA" + "</ocsp>");
			mensaje.append("</fullSignature>");
		mensaje.append("</createExitRegistryRequest>");
		return mensaje.toString();

	} */


    
    
    /**
     * Los códigos de provincia en el RT tienen que estar formados por 2 caracteres. En Flexia hay códigos de provincia que 
     * sólo están formados por uno sólo, por tanto, hay que añadir un cero a la izquierda para que 
     * @param provincia: Código de provincia
     * @return 
     */
    private String tratarCodigoProvinciaRT(String provincia){                        
        if(provincia!=null && provincia.length()==1){                
            provincia = "0" + provincia;                
        }
        return provincia;
    }
    
    

    /**
     * Crea una anotación de salida en el RT
     * @param arg0: ApunteRegistroVO cno los datos del objeto a dar de alta
     * @param codOrganizacion: Código de la organización
     * @param firma: firma de la notificación
     * @return
     * @throws es.altia.merlin.licitacion.exceptions.SleException
     */
    public ApunteRegistroVO crearSalida(es.altia.flexia.notificacion.vo.ApunteRegistroVO arg0,String codOrganizacion,String firma) throws SleException {
        log.debug("RegistroTelematico.crearSalida =============>");
		// Se convierte el objeto que nos llega en un String
		//String mensaje = crearMensajeExit(arg0,codOrganizacion);

        ResourceBundle config = ResourceBundle.getBundle("notificaciones");
        String urlWS = config.getString("URL_WS_TELEMATICO");
        String anotacionSalida = config.getString("ANNOTATION_EXIT");
        String tipoNotificacion = config.getString("NOTIFICATION");
        String codigoSLE = config.getString("CODIGO_SLE");

        String CODIGO_PROVINCIA_RT_DESCONOCIDO = config.getString("CODIGO_PROVINCIA_RT_DESCONOCIDO");
        String CODIGO_POSTAL_RT_DEFECTO = config.getString("CODIGO_POSTAL_RT_DEFECTO");

        log.debug("CODIGO_PROVINCIA_RT_DESCONOCIDO: " + CODIGO_PROVINCIA_RT_DESCONOCIDO + ",CODIGO_POSTAL_RT_DEFECTO: " + CODIGO_POSTAL_RT_DEFECTO);


       TelematicRegistry_PortType regTelem = null; 
       int timeOut = 300000; 
		try {
                     TelematicRegistry_ServiceLocator telematic = new TelematicRegistry_ServiceLocator();
                      regTelem = telematic.getTelematicRegistrySOAP(new java.net.URL(urlWS));

            
            if (regTelem!=null && regTelem instanceof Stub) {
                ((Stub) regTelem).setTimeout(timeOut);
            }//if

            // ENTRY DATA
            EntryData entryData = new EntryData();
            entryData.set_sAnnotationType(anotacionSalida);

            /********** SENDER PARTY. ORIGEN DE LA ANOTACIÓN ******************/
            SenderParty senderParty = new SenderParty();

            SenderUnit unit = new SenderUnit();
            unit.set_sDepartment(arg0.getCodOrganoOrigen());
            unit.set_sEntity(arg0.getEntidad());
            unit.set_sOrganization(arg0.getCodUnidadOrigen());
            senderParty.set_senderUnit(unit);
            entryData.set_senderParty(senderParty);


            DestinationPartyList destinationParty = new DestinationPartyList();
            /******************  TERCEROS DESTINATARIOS DE LA ANOTACIÓN  **************/
            DestinationApplicant destinationApplicant = new DestinationApplicant();
            ArrayList<DestinationApplicant> destino = new ArrayList<DestinationApplicant>();

            ArrayList<TerceroRegistroVO> terceros = arg0.getTerceros();
            for(int i=0;terceros!=null && i<terceros.size();i++){
                TerceroRegistroVO tercero = terceros.get(i);
            
                destinationApplicant.set_sCodeID(tercero.getNifTercero());
                destinationApplicant.set_sName(tercero.getNombreTercero());
                destinationApplicant.set_sSurname(tercero.getApellido1() + " " + tercero.getApellido2());
                destinationApplicant.set_sTypeID(tercero.getTipoDocumento());

                /******************  DIRECCIÓN DEL TERCERO ********************/
                Address address = new Address();
                DireccionRegistroVO direccion = tercero.getDireccion();
                address.set_sStreet(direccion.getDireccion());
                address.set_sCity(direccion.getDescripcionMunicipio());

                if(direccion.getCodigoProvincia()!=null && !"99".equals(direccion.getCodigoProvincia()) && !"999".equals(direccion.getCodigoProvincia()))
                    address.set_sProvince(tratarCodigoProvinciaRT(direccion.getCodigoProvincia()));
                else
                    address.set_sProvince(CODIGO_PROVINCIA_RT_DESCONOCIDO);

                address.set_sCountry(direccion.getCodigoPais());

                if(direccion.getCodigoPostal()==null || "".equals(direccion.getCodigoPostal()) || "null".equalsIgnoreCase(direccion.getCodigoPostal())){
                    address.set_sZipCode(CODIGO_POSTAL_RT_DEFECTO);
                }else
                    address.set_sZipCode(direccion.getCodigoPostal());
                
                String phone = (tercero.getTelefono()==null)?"":tercero.getTelefono();
                address.set_sPhoneNumber(phone);
                address.set_sFaxNumber("");
                address.set_sEmail((tercero.getEmail()==null)?"":tercero.getEmail());
               
                destinationApplicant.set_address(address);

                /****************** DIRECCIÓN DE NOTIFICACIÓN **********************/
                Notification notification = new Notification();

                notification.set_sNotificationType(tipoNotificacion);
                notification.set_sStreet(direccion.getDireccion());
                notification.set_sCity(direccion.getDescripcionMunicipio());
                 if(direccion.getCodigoProvincia()!=null && !"99".equals(direccion.getCodigoProvincia()) && !"999".equals(direccion.getCodigoProvincia()))
                    notification.set_sProvince(tratarCodigoProvinciaRT(direccion.getCodigoProvincia()));
                else
                    notification.set_sProvince(CODIGO_PROVINCIA_RT_DESCONOCIDO);
                notification.set_sCountry(direccion.getCodigoPais());
                
                //notification.set_sZipCode(direccion.getCodigoPostal());
                 if(direccion.getCodigoPostal()==null || "".equals(direccion.getCodigoPostal()) || "null".equalsIgnoreCase(direccion.getCodigoPostal())){
                    notification.set_sZipCode(CODIGO_POSTAL_RT_DEFECTO);
                }else
                    notification.set_sZipCode(direccion.getCodigoPostal());
                notification.set_sPhoneNumber(phone);
                notification.set_sFaxNumber("");
                notification.set_sEmail((tercero.getEmail()==null)?"":tercero.getEmail());                
                
                destinationApplicant.set_notification(notification);
            
                destino.add(destinationApplicant);
            }


            ArrayList<DestinationUnit> listaDestinoUnit = new ArrayList<DestinationUnit>();
            destinationParty.set_aDestinationUnit(listaDestinoUnit);

            destinationParty.set_aDestinationApplicant(destino);
            entryData.set_destinationPartyList(destinationParty);

            /*** SENDER APP INFO *****/
            SenderAppInfo appInfo = new SenderAppInfo();
            appInfo.set_sAppCode(codigoSLE);
            appInfo.set_sProcedureCode(arg0.getCodProcedimiento());
            appInfo.set_sFolderID(arg0.getNumExpediente());
            entryData.set_senderAppInfo(appInfo);

             
            entryData.set_sSubject(arg0.getAsunto());
            entryData.set_sBody(arg0.getTexto());
            
            DocumentList documentList = new DocumentList();
            ArrayList<DocumentUnit> listaDocumentUnit = new ArrayList<DocumentUnit>();
            documentList.set_aDocuments(listaDocumentUnit);
            entryData.set_documentList(documentList);

            /***** FIRMA *****/
            FullSignature signature = new FullSignature();
            if (firma != null){
                signature.set_sOcsp("NULL");
                signature.set_sSignature(firma);
                signature.set_sTimestamp("null");
                log.info("FIRMA");
            }//if (firmaSolicitud != null)
             log.info("antes de");

            //CreateEntryRequest exitRequest = new CreateEntryRequest();
            CreateExitRequest exitRequest = new CreateExitRequest();
            exitRequest.set_entryData(entryData);
            exitRequest.set_fullSignature(signature);

 
            SetExitRegistryRequestXML regXML = new SetExitRegistryRequestXML();
            Writer input = regXML.marshaller(exitRequest);
            log.info("input "+input);
            log.info("input.toString() "+input.toString());
            //SetEntryRegistryRequestXML registryRequest = new SetEntryRegistryRequestXML();
            //Writer input = registryRequest.marshaller(exitRequest);

            exitRequest = null;
            exitRequest = null;
 log.info("INICIO    - LLAMADA WS");
            String output = regTelem.setExit(input.toString());
            log.info("FIN    - LLAMADA WS");
            input = null;
            log.info("output: " + output);


            SetExitRegistryResultXML result = new SetExitRegistryResultXML();
            CreateExitResult createEntryRegistryResponse = (CreateExitResult) result.unmarshaller(new StringReader(output));


            String status = createEntryRegistryResponse.get_sStatus();
            log.debug("status: " + status);

            String numeroRT = null;
            String numeroFirmaServidor = null;
            String fechaHoraRegistro = null;
            if(status!=null && "OK".equalsIgnoreCase(status)){
                numeroRT = createEntryRegistryResponse.get_createExitRegistryResponse().get_registryData().get_sTelematicRegistryNumber();
                fechaHoraRegistro = createEntryRegistryResponse.get_createExitRegistryResponse().get_registryData().get_sTelematicRegistryDate();
                log.debug("numeroRT: " + numeroRT + ", numeroFirmaServidor: " + numeroFirmaServidor + ",fechaHoraRegistro: " + fechaHoraRegistro);
                arg0.setNumeroRegistroRT(numeroRT);
                arg0.setFechaRT(fechaHoraRegistro);
                return arg0;

            }

            
        } catch (MalformedURLException e) {
            log.error(".crearSalida() Expresión mal formada",e);
        } catch (ServiceException e) {
            log.error(".crearSalida() Error al crear entrada en el registro telemático",e);
        } catch (Exception e) {
            log.error(".crearSalida() Error inesperado invocando WS ", e);
        } 
        

		return null;
	}//crearSalida


  






}
