package es.altia.agora.webservice.tercero.servicios.arkhe;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.servicios.arkhe.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.arkhe.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.arkhe.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.arkhe.cliente.DatosDomicilioVO;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.config.Config;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.xml.rpc.ServiceException;
import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class BusquedaTerceroArkheImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroArkheImpl.class.getName());
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
            DatosTerceroVO[] resultadoSW = terceroPistService.findTercero(doi, tipoDocumento, nombre, apellido1, apellido2);
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
        for (DatosTerceroVO terceroSW : resultadoSW) {
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
        terceroSGE.setOrigen(nombreServicio);
        terceroSGE.setDomicilios(transformarDomicilioArraySW(terceroSW.getDomicilios()));
        terceroSGE.setSituacion('A');

        return terceroSGE;
    }

    private Vector<DomicilioSimpleValueObject> transformarDomicilioArraySW(DatosDomicilioVO[] resultadoSW) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();
        for (DatosDomicilioVO domicilioSW : resultadoSW) {
            arrayDomicilioSGE.add(transformarDomicilioSW(domicilioSW));
        }
        return arrayDomicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(DatosDomicilioVO domicilioSW) {

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(domicilioSW.getCodPostal());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setDomicilio(domicilioSW.getStrDireccion());
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
