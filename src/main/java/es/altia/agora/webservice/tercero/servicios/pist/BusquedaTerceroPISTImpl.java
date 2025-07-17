package es.altia.agora.webservice.tercero.servicios.pist;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.pist.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.pist.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.pist.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.pist.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.tercero.servicios.pist.cliente.DireccionWTO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import javax.xml.rpc.ServiceException;
import java.util.Vector;
import java.rmi.RemoteException;
import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BusquedaTerceroPISTImpl implements FachadaBusquedaTercero {

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

        DireccionWTO domicilio = new DireccionWTO();
        domicilio.setBloque(condsBusqueda.getBloque());
        domicilio.setCodigoPostal(condsBusqueda.getCodPostal());

        // EL EMPLAZAMIENTO HAY QUE VER DE DÓNDE SE SACA
        //domicilio.setDescripcionEmplazamiento(condsBusqueda.get);

        domicilio.setDescripcionVia(condsBusqueda.getNombreVia());
        if(condsBusqueda.getCodigoMunicipio()>-1)
            domicilio.setCodigoMunicipio(Integer.toString(condsBusqueda.getCodigoMunicipio()));

        if(condsBusqueda.getCodigoProvincia()>-1)
            domicilio.setCodigoProvincia(Integer.toString(condsBusqueda.getCodigoProvincia()));

        domicilio.setEscalera(condsBusqueda.getEscalera());        
        domicilio.setPlanta(condsBusqueda.getPlanta());
        domicilio.setPortal(condsBusqueda.getPortal());
        if(!"".equals(condsBusqueda.getDomicilio()))
            domicilio.setDescripcionEmplazamiento(condsBusqueda.getDomicilio());
        
        if(condsBusqueda.getNumeroDesde()>-1)
            domicilio.setPrimerNumero(Integer.toString(condsBusqueda.getNumeroDesde()));
        
        if(condsBusqueda.getNumeroHasta()>-1)
            domicilio.setUltimoNumero(Integer.toString(condsBusqueda.getNumeroHasta()));
        domicilio.setPuerta(condsBusqueda.getPuerta());
      

        try {
            URL urlEndPoint = new URL(strUrlEndPoint);
            WSBusquedaTerceroServiceLocator terceroPistLocator = new WSBusquedaTerceroServiceLocator();
            WSBusquedaTerceroPort terceroPistService = terceroPistLocator.getWSBusquedaTerceroEndPoint(urlEndPoint);
            DatosTerceroVO[] resultadoSW = terceroPistService.findTercero(doi, tipoDocumento, nombre, apellido1, apellido2,domicilio);                        
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
        terceroSGE.setOrigen("PIST");
        terceroSGE.setDomicilios(transformarDomicilioArraySW(terceroSW.getDomicilios()));
        terceroSGE.setSituacion('A');

        return terceroSGE;
    }

    private Vector<DomicilioSimpleValueObject> transformarDomicilioArraySW(DatosDomicilioVO[] resultadoSW) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();

        if(resultadoSW!=null){
            for (DatosDomicilioVO domicilioSW: resultadoSW) {
                arrayDomicilioSGE.add(transformarDomicilioSW(domicilioSW));
            }
        }//if
        return arrayDomicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(DatosDomicilioVO domicilioSW) {

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setBloque(domicilioSW.getBloque());
        domicilioSGE.setCodigoPostal(domicilioSW.getCodPostal());
        domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio());
        domicilioSGE.setProvincia(domicilioSW.getDescProvincia());
        domicilioSGE.setPais(domicilioSW.getDescPais());
        domicilioSGE.setEscalera(domicilioSW.getEscalera());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setLetraDesde(domicilioSW.getLetraDesde());
        domicilioSGE.setNumDesde(domicilioSW.getNumDesde());
        domicilioSGE.setPlanta(domicilioSW.getPlanta());
        domicilioSGE.setPortal(domicilioSW.getPortal());
        domicilioSGE.setPuerta(domicilioSW.getPuerta());

        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        domicilioSGE.setDescVia(domicilioSW.getVia().getNombre());

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoViaTAO = domicilioSW.getVia().getDescTipoVia();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoViaTAO, paramsConexionBD);
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        try {
            GeneralValueObject infoPais = fachadaSGE.getPaisByDescription(domicilioSW.getDescPais(), paramsConexionBD);
            String codPais = (String)infoPais.getAtributo("codigoPais");
            domicilioSGE.setIdPais(codPais);
            domicilioSGE.setIdPaisVia(codPais);

            if (!codPais.equals("108")) {
                m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setIdProvincia("66");
                domicilioSGE.setIdMunicipio(codPais);
                domicilioSGE.setIdPaisVia("108");
                domicilioSGE.setIdProvinciaVia("66");
                domicilioSGE.setIdMunicipioVia(codPais);

            } else {
                m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndDesc(Integer.parseInt(codPais),
                        domicilioSW.getDescProvincia(), paramsConexionBD);

                String codProvincia = (String)infoProvincia.getAtributo("codigoProvincia");
                domicilioSGE.setIdProvincia(codProvincia);
                domicilioSGE.setIdProvinciaVia(codProvincia);

                MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndDesc(Integer.parseInt(codPais),
                        Integer.parseInt(codProvincia), domicilioSW.getDescMunicipio(), paramsConexionBD);

                domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));

            }
            
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
        }

        return domicilioSGE;
    }
}
