package es.altia.agora.webservice.tercero.servicios.iasoft;

import es.altia.agora.webservice.tercero.servicios.iasoft.cliente.*;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class BusquedaTerceroIASoftImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroIASoftImpl.class.getName());
    
    private static final String IASOFT_PADRON_NAMESPACE = "http://www.red.es/padron";
    
    private final String VOLANTE_EMPADRONAMIENTO                     = "VolanteEmpadronamiento";
    
    private final String ESTADO = "Estado";
    
    private final String VOLANTE_EMPADRONAMIENTO_FIRMADOS = "VolanteEmpadronamientoDatosFirmados";
    
    private final String DATOS_PERSONALES                            = "DatosPersonales";
    private final String DATOS_PERSONALES_NOMBRE                     = "Nombre";
    private final String DATOS_PERSONALES_PRIMER_APELLIDO            = "PrimerApellido";
    private final String DATOS_PERSONALES_PARTICULA_PRIMER_APELLIDO  = "ParticulaPrimerApellido";
    private final String DATOS_PERSONALES_SEGUNDO_APELLIDO           = "SegundoApellido";
    private final String DATOS_PERSONALES_PARTICULA_SEGUNDO_APELLIDO = "ParticulaSegundoApellido";
    private final String DATOS_PERSONALES_TIPO_DOCUMENTACION         = "TipoDocumentacion";
    private final String DATOS_PERSONALES_DOCUMENTACION              = "Documentacion";
    
    private final String DIR_EMPADRONAMIENTO = "DireccionEmpadronamiento";
    private final String DIR_EMPADRONAMIENTO_LOCALIDAD = "Localidad";
    private final String DIR_EMPADRONAMIENTO_LOCALIDAD_PROVINCIA = "CodigoProvincia";
    private final String DIR_EMPADRONAMIENTO_LOCALIDAD_MUNICIPIO = "CodigoMunicipio";
    private final String DIR_EMPADRONAMIENTO_COD_POSTAL = "CodigoPostal";
    private final String DIR_EMPADRONAMIENTO_VIA = "Via";
    private final String DIR_EMPADRONAMIENTO_VIA_TIPO = "TipoVia";
    private final String DIR_EMPADRONAMIENTO_VIA_NOMBRE = "NombreVia";
    private final String DIR_EMPADRONAMIENTO_NUMERACION = "Numeracion";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_NUMERO = "Numero";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_CALIFICADOR = "CalificadorNumero";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_NUMERO_SUPERIOR = "NumeroSuperior";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_CALIFICADOR_SUPERIOR = "CalificadorNumeroSuperior";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_BLOQUE = "Bloque";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_PORTAL = "Portal";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_ESCALERA = "Escalera";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_PLANTA = "Planta";
    private final String DIR_EMPADRONAMIENTO_NUMERACION_PUERTA = "Puerta";
    
    private final String PREFIJO_PROP_TIPO_DOC = "TipoDocIASoft.";
    
    private final String CODIGO_PAIS = "108";
    private final String NOMBRE_PAIS = "ESPAÑA";
    
    private static Namespace n = Namespace.getNamespace(IASOFT_PADRON_NAMESPACE);            
    
    public String getNombreServicio() {
        return this.nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return this.prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException {
        
        m_Log.info("BUSQUEDA DE TERCEROS EN EL PADRON DE IASOFT");
                
        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");        
        String strCodPrvConfig = m_ConfigTerceros.getString(prefijoPropiedad + "codProvincia");
        String strCodMunConfig = m_ConfigTerceros.getString(prefijoPropiedad + "codMunicipio");
                
        try {            
            int codPrvConf = Integer.parseInt(strCodPrvConfig.trim());             
            int codMunConf = Integer.parseInt(strCodMunConfig.trim());           
            
            int codPrvBusq = condsBusqueda.getCodigoProvincia();            
            int codMunBusq = condsBusqueda.getCodigoMunicipio();
            
            if (codPrvConf != codPrvBusq) {
                if (codPrvBusq != -1) return new Vector();
            }
            
            if (codMunConf != codMunBusq) {
                if (codMunBusq != -1) return new Vector();
            }
            
        } catch (NumberFormatException nfe) {
             throw new EjecucionBusquedaTerceroException("ERROR EN LA CONFIGURACIÓN DEL SISTEMA DE BUSQUEDA", nfe);            
        }
        
        String documentacion = condsBusqueda.getDocumento();        
        if (documentacion == null || "".equals(documentacion.trim())) {
            return new Vector();
        }
                
        String strUrlEndPoint = m_ConfigTerceros.getString(prefijoPropiedad + "urlEndPoint");
                
        VolanteEResponse respuesta;
        try {
            
            VolanteE volante = new VolanteE(strCodPrvConfig, strCodMunConfig, documentacion);
            
            URL urlEndPoint = new URL(strUrlEndPoint);
            ServiciosLocator iaSoftLocator = new ServiciosLocator();
            ServiciosSoap iaSoftPort = iaSoftLocator.getServiciosSoap(urlEndPoint);
            respuesta = iaSoftPort.volanteE(volante);            
            
            return getTerceroValueObject(respuesta.getVolanteEResult(), params);            
            
        } catch (ServiceException se) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", se);
        } catch (RemoteException re) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", re);
        } catch (MalformedURLException mue) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mue);
        }                                
    }
    
    
    /**
     * Transforma la respuesta enviada por el padrón de IASOFT a un objeto TercerosValueObject
     * @param respuestaLlamada: Respuesta en formato xml enviada por el servicio web del padrón de IASOFT
     * @return TercerosValueObject
     */
    private Vector<TercerosValueObject> getTerceroValueObject(String respuestaLlamada, String[] params) throws EjecucionBusquedaTerceroException {
         
        try{ 
            // Se obtiene un objeto Document a partir del contenido del fichero
            SAXBuilder builder = new SAXBuilder();            
            Document doc = builder.build(new StringReader(respuestaLlamada));
                        
            Element root = doc.getRootElement();
            if(root == null) {
                throw new EjecucionBusquedaTerceroException("ERROR AL TRADUCIR LA INFORMACIÓN DEL TERCERO RECUPERADA", new Exception());
            }
            
            // Se comprueba que se recibe un código de estado valido.
            Element estadoElem = root.getChild(ESTADO);
            String estado = estadoElem.getValue();
            if (!estado.equals("00")) {
                if (estado.equals("03")) {
                    throw new EjecucionBusquedaTerceroException("ERROR PRODUCIDO EN EL SERVICIO DE BUSQEUDA", new Exception());
                } else return new Vector<TercerosValueObject>();
            }
            
            Element volante = root.getChild(VOLANTE_EMPADRONAMIENTO);
            
            if(volante == null) {
                throw new EjecucionBusquedaTerceroException("ERROR AL TRADUCIR LA INFORMACIÓN DEL TERCERO RECUPERADA" , new Exception());
            }
             
            String strVolante = volante.getValue();
            Document docDatos = builder.build(new StringReader(strVolante));
            Element rootDatosTercero = docDatos.getRootElement();
            
            Element datosTerceroFirmados = rootDatosTercero.getChild(VOLANTE_EMPADRONAMIENTO_FIRMADOS, n);            
            Element datosPersonalesElem = datosTerceroFirmados.getChild(DATOS_PERSONALES, n);
            TercerosValueObject tercero = parseTercero(datosPersonalesElem, params);
            
            // Ahora vamos a recuperar la inforamción del domicilio.
            Element dirEmpElem = datosTerceroFirmados.getChild(DIR_EMPADRONAMIENTO, n);
            Vector<DomicilioSimpleValueObject> domicilio = new Vector<DomicilioSimpleValueObject>();
            domicilio.add(parseDomicilio(dirEmpElem, params));
            
            tercero.setDomicilios(domicilio);
            
            Vector<TercerosValueObject> terceros = new Vector<TercerosValueObject>();
            terceros.add(tercero);
            
            return terceros;
                                        
        } catch(JDOMException jdome){
            throw new EjecucionBusquedaTerceroException("ERROR AL TRADUCIR LA INFORMACIÓN DEL TERCERO RECUPERADA" , jdome);                     
        } catch(IOException ioe){
            throw new EjecucionBusquedaTerceroException("ERROR AL TRADUCIR LA INFORMACIÓN DEL TERCERO RECUPERADA" , ioe);                                    
        }                 
    }

    private TercerosValueObject parseTercero(Element datosPersElem, String[] params) {
        
        TercerosValueObject parsedTercero = new TercerosValueObject();
                
        Element tipoDocElem = datosPersElem.getChild(DATOS_PERSONALES_TIPO_DOCUMENTACION, n);
        String tipoDocIaSoft = tipoDocElem.getValue();        
        Config tipoDocConfig = ConfigServiceHelper.getConfig("es.altia.agora.webservice.tercero.servicios.iasoft.TraductorTipoDocumento");
        String tipoDocSIGP = tipoDocConfig.getString(PREFIJO_PROP_TIPO_DOC + tipoDocIaSoft);        
        parsedTercero.setTipoDocumento(tipoDocSIGP);
        
        FachadaSGETercero fachadaSGE = new FachadaSGETercero();
        String descTipoDocSIGP = fachadaSGE.getDescripcionTipoDocumentoByCodigo(tipoDocSIGP, params);
        parsedTercero.setTipoDocDesc(descTipoDocSIGP);
        
        Element docElem = datosPersElem.getChild(DATOS_PERSONALES_DOCUMENTACION, n);
        String documentacion = docElem.getValue();
        parsedTercero.setDocumento(documentacion);
        
        Element nombreElem = datosPersElem.getChild(DATOS_PERSONALES_NOMBRE, n);
        String nombre = nombreElem.getValue();
        parsedTercero.setNombre(nombre);
        
        Element primApeElem = datosPersElem.getChild(DATOS_PERSONALES_PRIMER_APELLIDO, n);
        String primApellido = primApeElem.getValue();
        parsedTercero.setApellido1(primApellido);
        
        Element partPrimApeElem = datosPersElem.getChild(DATOS_PERSONALES_PARTICULA_PRIMER_APELLIDO, n);
        String partPrimApellido = partPrimApeElem.getValue();
        parsedTercero.setPartApellido1(partPrimApellido);
        
        Element segApeElem = datosPersElem.getChild(DATOS_PERSONALES_SEGUNDO_APELLIDO, n);
        String segApellido = segApeElem.getValue();
        parsedTercero.setApellido2(segApellido);
        
        Element partSegApeElem = datosPersElem.getChild(DATOS_PERSONALES_PARTICULA_SEGUNDO_APELLIDO, n);
        String partSegApellido = partSegApeElem.getValue();
        parsedTercero.setPartApellido2(partSegApellido);
        
        parsedTercero.setNormalizado("2");
        parsedTercero.setTelefono(null);
        parsedTercero.setEmail(null);
        parsedTercero.setIdentificador("0");
        parsedTercero.setOrigen(nombreServicio);
        parsedTercero.setSituacion('A');
        
        return parsedTercero;
        
    }
    
    private DomicilioSimpleValueObject parseDomicilio(Element dirEmpElem, String[] params) {
        
        DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
        domicilio.setIdPais(CODIGO_PAIS);
        domicilio.setIdPaisVia(CODIGO_PAIS);
        
        Element locElem = dirEmpElem.getChild(DIR_EMPADRONAMIENTO_LOCALIDAD, n);
        Element codProvElem = locElem.getChild(DIR_EMPADRONAMIENTO_LOCALIDAD_PROVINCIA, n);
        String codProvincia = codProvElem.getValue();
        domicilio.setIdProvincia(codProvincia);
        domicilio.setIdProvinciaVia(codProvincia);
        
        Element codMunElem = locElem.getChild(DIR_EMPADRONAMIENTO_LOCALIDAD_MUNICIPIO, n);
        String codMunicipio = codMunElem.getValue();
        domicilio.setIdMunicipio(codMunicipio);
        domicilio.setIdMunicipioVia(codMunicipio);
        
        int codigoPais = 108;
        int codigoProvincia = Integer.parseInt(codProvincia);
        int codigoMunicipio = Integer.parseInt(codMunicipio);        
        FachadaSGETercero fachadaSGE = new FachadaSGETercero();
        
        domicilio.setPais(NOMBRE_PAIS);
        
        try {
            GeneralValueObject infoProv = fachadaSGE.getProvinciaByPaisAndCodigo(codigoPais, codigoProvincia, params);
            String nombreProvincia = (String)infoProv.getAtributo("nombreProvincia");
            domicilio.setProvincia(nombreProvincia);
        
        
            MunicipioVO infoMun = fachadaSGE.getMunicipioByPaisAndProvAndCodigo(codigoPais, codigoProvincia, codigoMunicipio, params);
            String nombreMunicipio = infoMun.getNombreOficial();
            domicilio.setMunicipio(nombreMunicipio);
            
        } catch (Exception e) {
            domicilio.setIdProvincia("99");
            domicilio.setProvincia("DESCONOCIDA");
            domicilio.setIdMunicipio("999");
            domicilio.setMunicipio("DESCONOCIDO");
            domicilio.setIdPaisVia("108");
            domicilio.setIdProvinciaVia("99");
            domicilio.setIdMunicipioVia("999");
        }
                
        Element codPostalElem = dirEmpElem.getChild(DIR_EMPADRONAMIENTO_COD_POSTAL, n);
        String codigoPostal = codPostalElem.getValue();
        domicilio.setCodigoPostal(codigoPostal);
        
        Element viaElem = dirEmpElem.getChild(DIR_EMPADRONAMIENTO_VIA, n);
        Element tipoViaElem = viaElem.getChild(DIR_EMPADRONAMIENTO_VIA_TIPO, n);
        String nombreTipoVia = tipoViaElem.getValue();
        domicilio.setTipoVia(nombreTipoVia);
        
        try {
            GeneralValueObject infoTipoVia = fachadaSGE.getTipoViaByDescripcion(nombreTipoVia, params);
            String codTipoVia = (String)infoTipoVia.getAtributo("codTipoVia");
            domicilio.setIdTipoVia(codTipoVia);
        } catch (Exception e) {
            domicilio.setIdTipoVia("SV");
            domicilio.setTipoVia("SIN TIPO VIA");
        }
        
        domicilio.setCodigoVia("0");
        
        Element nombreViaElem = viaElem.getChild(DIR_EMPADRONAMIENTO_VIA_NOMBRE, n);
        String nombreVia = nombreViaElem.getValue();
        domicilio.setDescVia(nombreVia);
        
        Element numeracionElem = dirEmpElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION, n);
        Element numeroElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_NUMERO, n);
        String numDesde = numeroElem.getValue();
        domicilio.setNumDesde(numDesde);
        
        Element califElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_CALIFICADOR, n);
        String letraDesde = califElem.getValue();
        domicilio.setLetraDesde(letraDesde);
        
        Element numSupElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_NUMERO_SUPERIOR, n);
        String numHasta = numSupElem.getValue();
        domicilio.setNumHasta(numHasta);
        
        Element califSupElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_CALIFICADOR_SUPERIOR, n);
        String letraHasta = califSupElem.getValue();
        domicilio.setLetraHasta(letraHasta);
        
        Element bloqueElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_BLOQUE, n);
        String bloque = bloqueElem.getValue();
        domicilio.setBloque(bloque);
        
        Element portalElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_PORTAL, n);
        String portal = portalElem.getValue();
        domicilio.setPortal(portal);
        
        Element escaleraElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_ESCALERA, n);
        String escalera = escaleraElem.getValue();
        domicilio.setEscalera(escalera);
        
        Element plantaElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_PLANTA, n);
        String planta = plantaElem.getValue();
        domicilio.setPlanta(planta);
        
        Element puertaElem = numeracionElem.getChild(DIR_EMPADRONAMIENTO_NUMERACION_PUERTA, n);
        String puerta = puertaElem.getValue();
        domicilio.setPuerta(puerta);
                
        domicilio.setIdDomicilio("0");
        domicilio.setOrigen(nombreServicio);
        domicilio.setNormalizado("2");
        
        return domicilio;
    }

}
