package es.altia.flexia.portafirmasexternocliente.plugin.viafirma;

import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.flexia.portafirmasexternocliente.exception.PortafirmasExternoException;
import es.altia.flexia.portafirmasexternocliente.util.ConstantesPortafirmasExternoCliente;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import com.viafirma.tray.ws.server.ServiceWS;
import java.net.URL;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.BindingProvider;

/**
 * @author david.caamano
 * @version 05/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 05/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class ViaFirmaInboxWSClient {
    
    //Constantes parametros de conexion
    private final static String KEY = "key";
    private final static String USER = "user";
    private final static String URL = "url";
    private final static String QNAME_URL = "qname_url";
    private final static String QNAME_SERVICE = "qname_service";
    
    //Logger
    private static Logger log = Logger.getLogger(ViaFirmaInboxWSClient.class);
    
    //Bundle
    private static ResourceBundle ficheroPropiedades = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
    
    //Instance
    private static ViaFirmaInboxWSClient instance;
    
    //Propiedades
    private String url;
    private String user;
    private String key;
    private String qnameURL;
    private String qnameService;
    
    /**
     * Metodo que inicia las propiedades necesarias para instanciar el cliente del webservice del portafirmas
     * @param codOrganizacion 
     */
    private static void init(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("init() : BEGIN");
        instance = new ViaFirmaInboxWSClient();
        if(log.isDebugEnabled()) log.debug("Iniciamos las propiedades de conexion");
        instance.url = getUrl(codOrganizacion);
        instance.user = getUser(codOrganizacion);
        instance.key = getKey(codOrganizacion);
        instance.qnameURL = getQnameUrl(codOrganizacion);
        instance.qnameService = getQnameService(codOrganizacion);
        if(log.isDebugEnabled()) log.debug("init() : END");
    }//init
    
    /**
     * Metodo que recupera una instancia de la clase del cliente del webservice del portafirmas
     * @param codOrganizacion
     * @return 
     */
    public static ViaFirmaInboxWSClient getInstance(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            if(log.isDebugEnabled()) log.debug("Llamamos al metodo que inicia las propiedades de conexion");
            init(codOrganizacion);
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Recupera un objeto del cliente del webservice del portafirmas
     * 
     * @return ServiceWS
     * @throws PortafirmasExternoException 
     */
    public ServiceWS getService() throws PortafirmasExternoException{
        if(log.isDebugEnabled()) log.debug("getService() : BEGIN");
        ServiceWS serviceWS = null;
        try{
            if((qnameURL != null && !"".equalsIgnoreCase(qnameURL)) && (qnameService != null && !"".equalsIgnoreCase(qnameService))){
                QName qNameService = new QName(qnameURL, qnameService);
                if(url != null && !"".equalsIgnoreCase(url)){
                    URL wsdl = new URL(url + "?wsdl");
                    
                    Service service = Service.create(wsdl, qNameService);
                    serviceWS = service.getPort(ServiceWS.class);
                    if(user != null && !user.equals("") && key != null && !key.equals("")){
                        Map<String, Object> map=((BindingProvider)serviceWS).getRequestContext();
			map.put(BindingProvider.USERNAME_PROPERTY, user);
			map.put(BindingProvider.PASSWORD_PROPERTY, key);
			map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
                    }//if(user != null && !user.equals("") && key != null && !key.equals(""))
                }else{
                    throw new PortafirmasExternoException("Se ha producido un error, la URL del servicio web no existe");
                }//if(url != null && !"".equalsIgnoreCase(url))
            }else{
                throw new PortafirmasExternoException("Se ha producido un error instanciando el QNAME, no existen las propiedades necesarias");
            }//if((qnameURL != null && !"".equalsIgnoreCase(qnameURL)) && (qnameService != null && !"".equalsIgnoreCase(qnameService)))
        }catch(Exception ex){
            log.error("Se ha producido un error creando una instancia del cliente del servicio web " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getService() : END");
        return serviceWS;
    }//getService
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa al usuario del portafirmas
     * @param codOrganizacion
     * @return 
     */
    private static String getUser(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getUser() : BEGIN");
        String user = new String();
        try{
            user = ficheroPropiedades.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + ConstantesPortafirmasExternoCliente.BARRA +
                    getCodCliente(codOrganizacion) + ConstantesPortafirmasExternoCliente.BARRA + USER);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el usuario del portafirmas " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUser() : END");
        return user;
    }//getKey
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa a la clave de usuario del portafirmas
     * @param codOrganizacion
     * @return 
     */
    private static String getKey(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getKey() : BEGIN");
        String key = new String();
        try{
            key = ficheroPropiedades.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + ConstantesPortafirmasExternoCliente.BARRA +
                    getCodCliente(codOrganizacion) + ConstantesPortafirmasExternoCliente.BARRA + KEY);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la clave del portafirmas " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getKey() : END");
        return key;
    }//getKey
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa a la URL del portafirmas
     * @param codOrganizacion
     * @return 
     */
    private static String getUrl(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getUrl() : BEGIN");
        String url = new String();
        try{
            url = ficheroPropiedades.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + ConstantesPortafirmasExternoCliente.BARRA +
                    getCodCliente(codOrganizacion) + ConstantesPortafirmasExternoCliente.BARRA + URL);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la clave del portafirmas " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUrl() : END");
        return url;
    }//getKey
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa a la url del QNAME del portafirmas
     * @param codOrganizacion
     * @return 
     */
    private static String getQnameUrl(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getQnameUrl() : BEGIN");
        String qnameUrl = new String();
        try{
            qnameUrl = ficheroPropiedades.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + ConstantesPortafirmasExternoCliente.BARRA +
                    getCodCliente(codOrganizacion) + ConstantesPortafirmasExternoCliente.BARRA + QNAME_URL);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el qnameURL del portafirmas " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getQnameUrl() : END");
        return qnameUrl;
    }//getKey
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa al service name del QNAME del portafirmas
     * @param codOrganizacion
     * @return 
     */
    private static String getQnameService(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getQnameService() : BEGIN");
        String qnameService = new String();
        try{
            qnameService = ficheroPropiedades.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + ConstantesPortafirmasExternoCliente.BARRA +
                    getCodCliente(codOrganizacion) + ConstantesPortafirmasExternoCliente.BARRA + QNAME_SERVICE);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el qnameService del portafirmas " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getQnameService() : END");
        return qnameService;
    }//getKey
    
    /**
     * Recupera del fichero de propiedades la propiedad relativa al codigo del cliente del portafirmas definido para la organizacion
     * @param codOrganizacion
     * @return 
     */
    private static String getCodCliente(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getCodClientePortafirmasExterno() : BEGIN");
        String codClientePortafirmasExterno = new String("");
        try{
            ResourceBundle bundlePortafirmas = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String propCodClientePortafirmasExterno = bundlePortafirmas.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE);
            if(propCodClientePortafirmasExterno != null && !"".equalsIgnoreCase(propCodClientePortafirmasExterno)){
                if(log.isDebugEnabled()) log.debug("propCodClientePortafirmasExterno = " + propCodClientePortafirmasExterno);
                codClientePortafirmasExterno = propCodClientePortafirmasExterno;
            }//if(propCodClientePortafirmasExterno != null && !"".equalsIgnoreCase(propCodClientePortafirmasExterno))
        }catch(Exception e){ 
            log.error("Se ha producido un error recuperando el codigo del cliente del portafirmas externo "  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCodClientePortafirmasExterno() : END");
        return codClientePortafirmasExterno;
    }//getCodCliente
    
}//class
