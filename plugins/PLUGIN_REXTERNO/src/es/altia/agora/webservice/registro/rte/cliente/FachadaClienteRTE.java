package es.altia.agora.webservice.registro.rte.cliente;


import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.webservice.registro.exceptions.InstanciacionRegistroException;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.common.exception.TechnicalException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.telematicregistry.TelematicRegistry_PortType;
import es.altia.telematicregistry.TelematicRegistry_ServiceLocator;
import es.altia.telematicregistry.serialization.xml.GetReceiptRequestXML;
import es.altia.telematicregistry.serialization.xml.GetReceiptResultXML;
import es.altia.telematicregistry.serialization.xml.GetReceiptsInfoRequestXML;
import es.altia.telematicregistry.serialization.xml.GetReceiptsInfoResultXML;
import es.altia.telematicregistry.serialization.xml.objects.*;
import java.io.StringReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

public class FachadaClienteRTE {

    private static Log m_Log = LogFactory.getLog(FachadaClienteRTE.class.getName());
    private static FachadaClienteRTE instance;
    private TelematicRegistry_PortType ws;

    protected FachadaClienteRTE(String prefijoPropiedad) throws RegistroException {

        ResourceBundle conf = null;
        URL portAddress = null;
        
        try {
            if (m_Log.isDebugEnabled()){ m_Log.debug(this.getClass().getName() + " INICIO - Obteniendo conexion a web service RegistroTelematico");}
            conf = ResourceBundle.getBundle("Registro");
            String valorURL = prefijoPropiedad +  "urlEndPoint";
            if (m_Log.isDebugEnabled()){ m_Log.debug(this.getClass().getName() + " URL parametro: " + conf.getString(valorURL));}
            portAddress = new URL(conf.getString(valorURL));
            if (m_Log.isDebugEnabled()){ m_Log.debug(this.getClass().getName() + " URL: " + portAddress.toString());}
            new TelematicRegistry_ServiceLocator();
            TelematicRegistry_ServiceLocator telematic = new TelematicRegistry_ServiceLocator();
            ws = telematic.getTelematicRegistrySOAP(portAddress);
            if (m_Log.isDebugEnabled()){ m_Log.debug(this.getClass().getName() + " FIN    - Obteniendo conexion a web service RegistroTelematico");}
        } catch (ServiceException sex) {
             m_Log.error(this.getClass().getName() +  " Error -> " + sex.getMessage());
              throw new InstanciacionRegistroException(this.getClass().getName() + " : Error ", sex);
        } catch (MalformedURLException muex) {
            m_Log.error(this.getClass().getName() +  " Error -> " + muex.getMessage());
            throw new InstanciacionRegistroException(this.getClass().getName() + " : Error ", muex);
        } catch (Exception ex) {
            m_Log.error(this.getClass().getName() +  " Error -> "  + ex.getMessage());
            throw new InstanciacionRegistroException(this.getClass().getName() + " : Error ", ex);
        }
       
    }

    public static FachadaClienteRTE getInstance(String prefijoPropiedad) throws RegistroException {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(FachadaClienteRTE.class) {
                if (instance == null)
                    instance = new FachadaClienteRTE(prefijoPropiedad);
            }
        }
        return instance;
    }

    
    public  ArrayList<AsientoFichaExpedienteVO>  buscarEntradas(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, 
                                               String[] params, String nombreServicio) throws RegistroException {
        ArrayList<AsientoFichaExpedienteVO> salida = null;
        try{
         if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarEntradas() : WS inicializado ");}
            ArrayList<AsientoFichaExpedienteVO> asientosRegistrados = FichaExpedienteDAO.getInstance().cargaListaAsientosExternosExpediente(generalVO, nombreServicio, params);
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarEntradas() : Recuperados  " + asientosRegistrados.size() + "registros externos para el expediente");}
            int i=0;
            
            for(AsientoFichaExpedienteVO asientoReg:asientosRegistrados){
                if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarEntradas() : Procesando registro numero   " + i);}
                
                //Componemos el numero de registro con los datos de BBDD
                StringBuffer sb = new StringBuffer();
                
                //Concatenamos tipo de asiento
                if (asientoReg.getTipoAsiento()!=null && !asientoReg.getTipoAsiento().equals("")){sb.append(asientoReg.getTipoAsiento());}
                
                //Concatenamos fecha de registro
                if(asientoReg.getFechaAsiento()!= null && !asientoReg.getFechaAsiento().equals("")){sb.append(asientoReg.getFechaAsiento().replace("/",""));}
                
                //Concatenamos el numero de registro en si completado con 0 a la izquierda
                if (asientoReg.getNumeroAsiento()!= null && !asientoReg.getNumeroAsiento().equals("")){
                    String strNumAsiento = String.valueOf(asientoReg.getNumeroAsiento());
                    //sb.append(strNumAsiento.format("%1$-11s", strNumAsiento));
                    sb.append(String.format("%1$-11s", strNumAsiento));
                }
                
               GeneralValueObject asiento = buscarAsiento(sb.toString());
               AsientoFichaExpedienteVO asientoRecuperado = new AsientoFichaExpedienteVO();
               
               if (asientoRecuperado!=null){
                   asientoRecuperado.setOrigenAsiento(nombreServicio);
                   if (salida==null){salida=new ArrayList<AsientoFichaExpedienteVO>();}
                   
                   
                   String municipio =(String)generalVO.getAtributo("codMunicipio");
                   
                   /*
                   rVO.setMunProcedimiento(municipio);
                   rVO.setCodProcedimiento((String)generalVO.getAtributo("codProcedimiento")); 
                   if (generalVO.getAtributo("ejercicio").getClass().getName().equals("java.lang.String")){
                       rVO.setAnoReg(Integer.parseInt((String)generalVO.getAtributo("ejercicio")));
                   }else{
                       rVO.setAnoReg((Integer)generalVO.getAtributo("ejercicio"));
                   }
                   rVO.setNumExpediente((String)generalVO.getAtributo("numero"));
                   
                   rVO.setTipoDoc((String)asiento.getAtributo("tipoDocumentoInteresado"));
                   rVO.setDocumentoInteresado((String)asiento.getAtributo("documentoInteresado"));
                   rVO.setNombreInteresadoExterno((String)asiento.getAtributo("nombreInteresado"));
                   rVO.setApellido1InteresadoExterno((String)asiento.getAtributo("apellidosInteresado"));
                     */         
                  
                   asientoRecuperado.setAsuntoAsiento((String)asiento.getAtributo("asuntoAsiento"));
                   asientoRecuperado.setFechaAsiento((String)asiento.getAtributo("fechaAsiento"));
                   asientoRecuperado.setNumeroAsiento((Long)asiento.getAtributo("numeroAsiento"));
                   asientoRecuperado.setEjercicioAsiento((Integer)asiento.getAtributo("ejercicioAsiento"));
                   
                   asientoRecuperado.setNombreInteresado((String)asiento.getAtributo("nombreInteresado"));
                   asientoRecuperado.setApellido1Interesado((String)asiento.getAtributo("apellidosInteresado"));
                   String numRegistroRT = sb.toString();                   
                   asientoRecuperado.setTipoAsiento("E");
                   if(numRegistroRT!=null && !"".equals(numRegistroRT))
                       asientoRecuperado.setTipoAsiento(numRegistroRT.substring(0,1));
                   
                   
                   if (municipio!= null && !municipio.equals("")){
                    //rVO = cargarDatosInteresado(rVO, params);
                    //asientoRecuperado.setApellido1Interesado(rVO.getNomCompletoInteresado());
                   }
                   salida.add(asientoRecuperado);
               }
                
                i++;
               
            }

        } catch (Exception ex) {
            m_Log.error(this.getClass().getName() + ".buscarEntradas() : ERROR: " + ex.getMessage());
            throw new RegistroException(ex, ex.getMessage());       
        }
        return salida;
    }
    
    //private  AsientoFichaExpedienteVO buscarAsiento(String numeroRegistro)throws RegistroException{
    private  GeneralValueObject buscarAsiento(String numeroRegistro)throws RegistroException{
        //AsientoFichaExpedienteVO salida = null;
        GeneralValueObject salida = new GeneralValueObject();
        try{
                if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarAsiento() : INICIO ");}                      
                Writer input;
                
               ReceiptRequest receiptRequest = new ReceiptRequest();
                receiptRequest.set_sTelematicRegistryNumber(numeroRegistro);
                              
                input = new GetReceiptRequestXML().marshaller(receiptRequest);
                String output = ws.getReceipt(input.toString());
                
                
                GetReceiptResultXML getReceiptResultXML = new GetReceiptResultXML();                
                ReceiptResult receiptResult = (ReceiptResult)getReceiptResultXML.unmarshaller(new StringReader(output));
                
                String status = receiptResult.get_sStatus();
                if(status!=null && "OK".equalsIgnoreCase(status)){
                   if(receiptResult.get_getReceiptResponse()!=null){
                       ReceiptResponse response = receiptResult.get_getReceiptResponse();
                       
                       if(response!=null && response.get_registryData()!=null && response.get_registryData().get_annotationData()!=null){
                           
                           RegistryData registryData = response.get_registryData();
                           m_Log.debug("Fecha: " + registryData.get_sGeneralRegistryDate());
                           m_Log.debug("num: " + registryData.get_sTelematicRegistryNumber());
                
                           
                           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                           
                           SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm");                           
                           SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm");                           
                           String subject = registryData.get_annotationData().get_entryData().get_sSubject();
                           SenderParty sender = registryData.get_annotationData().get_entryData().get_senderParty();
                           String sFecha = registryData.get_sTelematicRegistryDate();
                           
                           
                           //salida = new AsientoFichaExpedienteVO();
                           
                           m_Log.debug("fechas. " + sFecha);
                           m_Log.debug("get_sGeneralRegistryNumber. " + registryData.get_sGeneralRegistryNumber());
                           m_Log.debug("get_sTelematicRegistryDate. " + registryData.get_sTelematicRegistryDate());
                           m_Log.debug("get_sTelematicRegistryNumber. " + registryData.get_sTelematicRegistryNumber());
                           
                           Date fecha = (Date)sdf2.parse(sFecha); 
                           
                           salida.setAtributo("asuntoAsiento",subject);
                           salida.setAtributo("fechaAsiento",sdf.format(fecha));
                           salida.setAtributo("horaAsiento",sdf3.format(fecha));
                           salida.setAtributo("numeroAsiento",Long.parseLong(registryData.get_sTelematicRegistryNumber().substring(9)));
                           salida.setAtributo("ejercicioAsiento",Integer.parseInt(sdf2.format(fecha).substring(6,10)));
                                                      
                           m_Log.debug("asunto. " + (String)salida.getAtributo("asuntoAsiento"));
                           m_Log.debug("fecha. " + (String)salida.getAtributo("fechaAsiento"));
                           m_Log.debug("numero. " + (Long)salida.getAtributo("numeroAsiento"));
                           m_Log.debug("ejercicio. " + (Integer)salida.getAtributo("ejercicioAsiento"));
                           
                           if(sender!=null){                           
                               String nombre = sender.get_senderApplicant().get_sName();
                               String apellidos = sender.get_senderApplicant().get_sSurname();
                               
                               m_Log.debug("tipo id: " + sender.get_senderApplicant().get_sTypeID());
                               m_Log.debug("sCode id: " + sender.get_senderApplicant().get_sCodeID());
                               salida.setAtributo("nombreInteresado",nombre);
                               salida.setAtributo("apellidosInteresado",apellidos);
                               salida.setAtributo("tipoAsiento","E");
                               
                               String tipoDocumento = sender.get_senderApplicant().get_sTypeID();
                               String documento = sender.get_senderApplicant().get_sCodeID();
                               salida.setAtributo("tipoDocumentoInteresado",tipoDocumento);
                               salida.setAtributo("documentoInteresado",documento);
                               
                               
                               Address direccion = sender.get_senderApplicant().get_address();
                               System.out.println("city:" + direccion.get_sCity());
                               System.out.println("get_sCountry:" + direccion.get_sCountry());
                               System.out.println("get_sEmail:" + direccion.get_sEmail());
                               System.out.println("get_sFaxNumber:" + direccion.get_sFaxNumber());
                               System.out.println("get_sProvince:" + direccion.get_sProvince());
                               System.out.println("get_sStreet:" + direccion.get_sStreet());
                               System.out.println("get_sZipCode:" + direccion.get_sZipCode());
                                                              
                               if(direccion!=null){
                                    salida.setAtributo("codPostalInteresado",direccion.get_sZipCode());
                                    salida.setAtributo("calleInteresado",direccion.get_sStreet());
                                    salida.setAtributo("municipioInteresado",direccion.get_sCity());
                                    if(direccion!=null && direccion.get_sProvince()!=null && "0".equals(direccion.get_sProvince())){
                                        // Provincia desconocida
                                        salida.setAtributo("provinciaInteresado","99");
                                    }else
                                        salida.setAtributo("provinciaInteresado",direccion.get_sProvince());
                               }
                               
                                              
                               System.out.println("nombre: " + nombre + ", apellidos: " + apellidos);
                               
                           }
                           
                       }                       
                   }
                }else {
                        String errorMessage = receiptResult.get_sErrorMessage();
                        m_Log.error(this.getClass().getName() + ".buscarAsiento() : ERROR: " + errorMessage);
                        throw new RegistroException(new Exception(), errorMessage);
                }
                
                
        } catch (TechnicalException techExc) {
            m_Log.error(this.getClass().getName() + ".buscarAsiento() : ERROR: " + techExc.getMessage());
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (RemoteException rExc) {
            m_Log.error(this.getClass().getName() + ".buscarAsiento() : ERROR: " + rExc.getMessage());
            throw new RegistroException(rExc, rExc.getMessage());   
        
        } catch (Exception ex) {
            m_Log.error(this.getClass().getName() + ".buscarEntradas() : ERROR: " + ex.getMessage());
            throw new RegistroException(ex, ex.getMessage());       
        }
        return salida;
    }

 public  RegistroValueObject  buscarEntrada(RegistroValueObject generalVO,  
                                               String[] params, String nombreServicio) throws RegistroException {
        RegistroValueObject salida = null;
        try{
         if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarEntrada() : WS inicializado ");}
            AsientoFichaExpedienteVO asientoReg= FichaExpedienteDAO.getInstance().cargaAsientoExterno(generalVO,  params);
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".buscarEntrada() : Recuperados el registro externo");}
            if (asientoReg!=null){
                //Componemos el numero de registro con los datos de BBDD
                StringBuffer sb = new StringBuffer();
                
                //Concatenamos tipo de asiento
                if (asientoReg.getTipoAsiento()!=null && !asientoReg.getTipoAsiento().equals("")){sb.append(asientoReg.getTipoAsiento());}
                
                //Concatenamos fecha de registro
                if(asientoReg.getFechaAsiento()!= null && !asientoReg.getFechaAsiento().equals("")){sb.append(asientoReg.getFechaAsiento().replace("/",""));}
                
                //Concatenamos el numero de registro en si completado con 0 a la izquierda
                if (asientoReg.getNumeroAsiento()!= null && !asientoReg.getNumeroAsiento().equals("")){
                    String strNumAsiento = String.valueOf(asientoReg.getNumeroAsiento());
                    //sb.append(strNumAsiento.format("%1$-11s", strNumAsiento));
                    sb.append(String.format("%1$-11s", strNumAsiento));
                }
                
                //AsientoFichaExpedienteVO asientoRecuperado = buscarAsiento(sb.toString());
                GeneralValueObject asientoRecuperado = buscarAsiento(sb.toString());
                
                if (asientoRecuperado!=null){                   
                    
                   //salida = new RegistroValueObject();
                    salida = generalVO;
                   salida.setAsunto((String)asientoRecuperado.getAtributo("asuntoAsiento"));
                   salida.setAnoReg((Integer)asientoRecuperado.getAtributo("ejercicioAsiento"));
                   salida.setNumReg((Long)asientoRecuperado.getAtributo("numeroAsiento"));
                   
                   String nombreCompleto = (String)asientoRecuperado.getAtributo("apellidosInteresado") + ", " + (String)asientoRecuperado.getAtributo("nombreInteresado");
                   salida.setNombreInteresadoExterno((String)asientoRecuperado.getAtributo("nombreInteresado"));
                   salida.setApellido1InteresadoExterno((String)asientoRecuperado.getAtributo("apellidosInteresado"));
                   salida.setNomCompletoInteresado(nombreCompleto);
                   salida.setTipoDoc((String)asientoRecuperado.getAtributo("tipoDocumentoInteresado"));
                   salida.setDocumentoInteresado((String)asientoRecuperado.getAtributo("documentoInteresado"));
                   salida.setTipoRegOrigen("RTE");
                   salida.setIdServicioOrigen("RTE");
                   
                   String horaAsiento = (String)asientoRecuperado.getAtributo("horaAsiento");
                   String fechaAsiento = (String)asientoRecuperado.getAtributo("fechaAsiento");
                                      
                   salida.setFecEntrada(fechaAsiento);
                   salida.setFecHoraDoc(fechaAsiento);                   
                   salida.setFechaDoc(fechaAsiento);
                   salida.setFechaDocu(fechaAsiento);
                   
                   
                   
                   
                                              
                   //SimpleDateFormat sdfOrigen = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                   //Date fecha = sdfOrigen.parse(asientoRecuperado.getFechaAsiento());
                   //SimpleDateFormat sdfDestino = new SimpleDateFormat("dd/MM/yyyy");                   
                   //salida.setFecEntrada(sdfDestino.format(fecha));
                   
                   
                   Vector<RegistroValueObject> vTemas = new  Vector<RegistroValueObject>();
                   /*
                   salida.setListaActuaciones(generalVO.getListaActuaciones());
                   salida.setListaAsuntos(generalVO.getListaAsuntos());
                   salida.setListaDepartamentos(generalVO.getListaDepartamentos());
                   salida.setListaDocsAsignados(generalVO.getListaDocsAsignados());                                      
                   salida.setListaFormulariosAnexos(generalVO.getListaFormulariosAnexos());
                   salida.setListaOrganizacionesExternas(generalVO.getListaOrganizacionesExternas());
                   salida.setListaProcedimientos(generalVO.getListaProcedimientos()); */                   
                   salida.setListaTemasAsignados(vTemas);                                                         
                   salida.setListaTiposIdInteresado(generalVO.getListaTiposIdInteresado()); 
                  
               }
           }

        } catch (Exception ex) {
            m_Log.error(this.getClass().getName() + ".buscarEntrada() : ERROR: " + ex.getMessage());
            throw new RegistroException(ex, ex.getMessage());       
        }
        return salida;
    }
 
    public  RegistroValueObject cargarDatosInteresado(RegistroValueObject registroVO, String[] params) throws RegistroException{
        try{
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosInteresado() : INICIO ");}
            InteresadoExpedienteVO terceroVO = new InteresadoExpedienteVO();
            
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio",registroVO.getMunProcedimiento());
            gVO.setAtributo("codProcedimiento",registroVO.getCodProcedimiento());
            gVO.setAtributo("ejercicio", String.valueOf(registroVO.getAnoReg()));
            gVO.setAtributo("numero",registroVO.getNumExpediente());
            
            
            Vector<InteresadoExpedienteVO> tercerosEncontrados = InteresadosManager.getInstance().getListaInteresados(gVO, params);
            
            
            if(tercerosEncontrados.size() > 0 ) {
                terceroVO = (InteresadoExpedienteVO) tercerosEncontrados.get(0);
                // Consigo los datos del tercero
                registroVO.setTipoDocInteresado(terceroVO.getTipoDoc());
                registroVO.setDocumentoInteresado(terceroVO.getTxtDoc());
                registroVO.setTipoRegOrigen("SGE");
                registroVO.setCodTerceroExterno(null);
                //formato del interesado
                String nombre = terceroVO.getNombreCompleto();
                registroVO.setNomCompletoInteresado(nombre);
                registroVO.setTlfInteresado(terceroVO.getTelf());
                registroVO.setEmailInteresado(terceroVO.getEmail());
                registroVO.setDomCompletoInteresado(terceroVO.getDomicilio());
             }

            
        }catch (Exception ex) {
            m_Log.error(this.getClass().getName() + ".cargarDatosInteresado() : ERROR: " + ex.getMessage());
            throw new RegistroException(ex, ex.getMessage());       
        }
        return registroVO;
    }
    
    public  Vector getInteresados(RegistroValueObject registroVO, String[] params) throws RegistroException{
        Vector<GeneralValueObject> salida = null;
        try{
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getInteresado() : INICIO ");}
            
            
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio",registroVO.getMunProcedimiento());
            gVO.setAtributo("codProcedimiento",registroVO.getCodProcedimiento());
            gVO.setAtributo("ejercicio", String.valueOf(registroVO.getAnoReg()));
            gVO.setAtributo("numero",registroVO.getNumExpediente());
            
            
            Vector<InteresadoExpedienteVO> tercerosEncontrados = InteresadosManager.getInstance().getListaInteresados(gVO, params);
            
            if(tercerosEncontrados.size() > 0 ) {
                if (salida==null){salida = new   Vector<GeneralValueObject>();}
                InteresadoExpedienteVO terceroVO = new InteresadoExpedienteVO();
                terceroVO = (InteresadoExpedienteVO) tercerosEncontrados.get(0);
                GeneralValueObject genVO = new GeneralValueObject();
                
                genVO.setAtributo("codigoTercero", String.valueOf(terceroVO.getCodTercero()));
                genVO.setAtributo("titular",terceroVO.getNombreCompleto());
	        genVO.setAtributo("versionTercero",String.valueOf(terceroVO.getNumVersion()));
	        genVO.setAtributo("rol",String.valueOf(terceroVO.getCodigoRol()));
	        genVO.setAtributo("descRol",terceroVO.getDescRol());
	        genVO.setAtributo("domicilio",terceroVO.getDomicilio());
                genVO.setAtributo("descDomicilio",terceroVO.getDomicilio());
	        genVO.setAtributo("telefono",terceroVO.getTelf());
                genVO.setAtributo("tip",terceroVO.getTipoDoc());
                genVO.setAtributo("doc",terceroVO.getTxtDoc());
	        genVO.setAtributo("email",terceroVO.getEmail());
	        genVO.setAtributo("cp",terceroVO.getCp());
	        genVO.setAtributo("pais",terceroVO.getPais());
	        genVO.setAtributo("provincia",terceroVO.getProvincia());
	        genVO.setAtributo("municipio",terceroVO.getMunicipio());
                genVO.setAtributo("porDefecto","SI");
                
                
                salida.add(genVO);
             }

            
        }catch (Exception ex) {
            m_Log.error(this.getClass().getName() + ".getInteresado() : ERROR: " + ex.getMessage());
            throw new RegistroException(ex, ex.getMessage());       
        }
        return salida;
    }
}
