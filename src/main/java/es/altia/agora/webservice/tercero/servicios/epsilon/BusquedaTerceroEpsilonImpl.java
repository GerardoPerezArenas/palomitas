package es.altia.agora.webservice.tercero.servicios.epsilon;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.pist.BusquedaTerceroPISTImpl;
import es.altia.agora.webservice.tercero.servicios.epsilon.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.epsilon.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.epsilon.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.epsilon.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.servicios.epsilon.cliente.DireccionWTO;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.Vector;
import java.util.StringTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;

public class BusquedaTerceroEpsilonImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroPISTImpl.class.getName());
    private FachadaSGETercero fachadaSGE = new FachadaSGETercero();

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException {

        paramsConexionBD = params;
        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");

        String strUrlEndPoint = m_ConfigTerceros.getString(prefijoPropiedad + "urlEndPoint");

        String doi = condsBusqueda.getDocumento();
        String tipoDocumento = Integer.toString(condsBusqueda.getTipoDocumento());
        String nombre = condsBusqueda.getNombre();
        String apellido1 = condsBusqueda.getApellido1();
        String apellido2 = condsBusqueda.getApellido2();

        try {
            URL urlEndPoint = new URL(strUrlEndPoint);
            WSBusquedaTerceroServiceLocator terceroPistLocator = new WSBusquedaTerceroServiceLocator();
            WSBusquedaTerceroPort terceroPistService = terceroPistLocator.getWSBusquedaTerceroEndPoint(urlEndPoint);

            DatosTerceroVO[] resultadoSW = terceroPistService.findTercero(doi, tipoDocumento, nombre, 
                    apellido1, apellido2, new DireccionWTO());
            return transformarTerceroArraySW(resultadoSW);

        } catch (ServiceException se) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", se);
        } catch (RemoteException re) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", re);
        } catch (MalformedURLException mue) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mue);
        }
    }

    private Vector<TercerosValueObject> transformarTerceroArraySW(DatosTerceroVO[] resultadoSW) {

        Vector<TercerosValueObject> arrayTerceroSGE = new Vector<TercerosValueObject>();
        for (DatosTerceroVO terceroSW: resultadoSW) {
            arrayTerceroSGE.add(transformarTerceroSW(terceroSW));
        }
        return arrayTerceroSGE;
    }

    private TercerosValueObject transformarTerceroSW(DatosTerceroVO terceroSW) {

        TercerosValueObject terceroSGE = new TercerosValueObject();
        terceroSGE.setApellido1(terceroSW.getApellido1());
        terceroSGE.setApellido2(terceroSW.getApellido2());
        terceroSGE.setNombre(terceroSW.getNombre());
        terceroSGE.setIdentificador("0");
        terceroSGE.setTelefono(terceroSW.getTelefono());
        terceroSGE.setTipoDocumento(terceroSW.getTipoDocumento());
        terceroSGE.setDocumento(terceroSW.getDoi());
        terceroSGE.setOrigen("Epsilon");
        terceroSGE.setDomicilios(transformarDomicilioArraySW(terceroSW.getDomicilios()));
        terceroSGE.setSituacion('A');

        return terceroSGE;
    }

    private Vector<DomicilioSimpleValueObject> transformarDomicilioArraySW(DatosDomicilioVO[] resultadoSW) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();
        for (DatosDomicilioVO domicilioSW: resultadoSW) {
            arrayDomicilioSGE.add(transformarDomicilioSW(domicilioSW));
        }
        return arrayDomicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(DatosDomicilioVO domicilioSW) {

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(domicilioSW.getCodPostal());
        domicilioSGE.setIdDomicilio("0");


        String numero = domicilioSW.getNumDesde();
        try {
            Integer.parseInt(numero);
            domicilioSGE.setNumDesde(numero);
        } catch (NumberFormatException nfe) {
            if (numero.indexOf("-") != -1) {
                StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                String numDesde = "";
                String numHasta ="";
                
                if (tokenizer.hasMoreTokens()) {
                    numDesde=tokenizer.nextToken();
                }
                if (tokenizer.hasMoreTokens()){
                    numHasta=tokenizer.nextToken();
                }  
                 
                boolean existeNumDesde=false;
                try {
                Integer.parseInt(numDesde);
                domicilioSGE.setNumDesde(numDesde);
                existeNumDesde=true;
                } catch (NumberFormatException nfe1) {
                    if(numDesde.length()==1) domicilioSGE.setLetraDesde(numDesde);
                    else if (numDesde.length()<=3)domicilioSGE.setBloque(numDesde);
                    
                    try {
                        Integer.parseInt(numHasta);
                        domicilioSGE.setNumDesde(numHasta);
                    } catch (NumberFormatException nfe2) {
                        if(numHasta.length()==1) domicilioSGE.setLetraDesde(numHasta);
                        else if (numHasta.length()<=3)domicilioSGE.setBloque(numHasta);
                    }
                    
                }
                if(existeNumDesde){
                    try {
                        Integer.parseInt(numHasta);
                        domicilioSGE.setNumHasta(numHasta);
                    } catch (NumberFormatException nfe1) {
                        if(numHasta.length()==1) domicilioSGE.setLetraDesde(numHasta);
                        else if (numHasta.length()<=3)domicilioSGE.setBloque(numHasta);
                    }
                }
                
                
               
            } else {
                domicilioSGE.setNumDesde("");
            }
        }
        domicilioSGE.setPlanta(domicilioSW.getPlanta());
        domicilioSGE.setPuerta(domicilioSW.getPuerta());

        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        domicilioSGE.setDescVia(domicilioSW.getVia().getNombre());

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoViaEpsilon = domicilioSW.getVia().getDescTipoVia();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoViaEpsilon, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia = fachadaSGE.getTipoViaByDescripcion(abrvTipoViaEpsilon, paramsConexionBD);
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

        try {
            if (!domicilioSW.getCodPais().equals("0")) {
                GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt(domicilioSW.getCodPais()), paramsConexionBD);
                String codPais = (String) infoPais.getAtributo("codigoPais");
                domicilioSGE.setIdPais(codPais);
                domicilioSGE.setIdPaisVia(codPais);
                String descPais = (String) infoPais.getAtributo("nombrePais");
                domicilioSGE.setPais(descPais);

                if (!codPais.equals("108")) {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                    domicilioSGE.setIdPais("108");
                    domicilioSGE.setPais("ESPAÑA");
                    domicilioSGE.setIdProvincia("66");
                    domicilioSGE.setProvincia("EXTRANJERO");
                    domicilioSGE.setIdMunicipio(codPais);
                    domicilioSGE.setMunicipio(descPais);
                    domicilioSGE.setIdPaisVia("108");
                    domicilioSGE.setIdProvinciaVia("66");
                    domicilioSGE.setIdMunicipioVia(codPais);

                } else {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                    GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndCodigo(Integer.parseInt(codPais),
                            Integer.parseInt(domicilioSW.getCodProvincia()), paramsConexionBD);

                    String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codProvincia);
                    domicilioSGE.setIdProvinciaVia(codProvincia);
                    String descProvincia = (String) infoProvincia.getAtributo("nombreProvincia");
                    domicilioSGE.setProvincia(descProvincia);

                    MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndCodigo(Integer.parseInt(codPais),
                            Integer.parseInt(codProvincia), Integer.parseInt(domicilioSW.getCodMunicipio()), paramsConexionBD);

                    domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                    domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                    domicilioSGE.setMunicipio(infoMunicipio.getNombreOficial());

                }
            } else {
                m_Log.debug("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setPais("ESPAÑA");
                domicilioSGE.setIdProvincia("99");
                domicilioSGE.setProvincia("DESCONOCIDA");
                domicilioSGE.setIdMunicipio("999");
                domicilioSGE.setMunicipio("DESCONOCIDO");
                domicilioSGE.setIdPaisVia("108");
                domicilioSGE.setIdProvinciaVia("99");
                domicilioSGE.setIdMunicipioVia("999");
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
        }

        return domicilioSGE;
    }
}
