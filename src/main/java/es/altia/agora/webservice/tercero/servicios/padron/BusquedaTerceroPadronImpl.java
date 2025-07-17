package es.altia.agora.webservice.tercero.servicios.padron;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.padron.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.padron.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;
import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class BusquedaTerceroPadronImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroPadronImpl.class.getName());
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
        terceroSGE.setOrigen(nombreServicio);
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
        domicilioSGE.setBloque("");
        domicilioSGE.setCodigoPostal("");
        domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio());
        domicilioSGE.setProvincia(domicilioSW.getDescProvincia());
        domicilioSGE.setPais(domicilioSW.getDescPais());
        domicilioSGE.setEscalera(domicilioSW.getEscalera());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setLetraDesde("");
        domicilioSGE.setNumDesde(domicilioSW.getNumDesde());
        domicilioSGE.setPlanta(domicilioSW.getPlanta());
        domicilioSGE.setPortal(domicilioSW.getPortal());
        domicilioSGE.setPuerta(domicilioSW.getPuerta());

        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        domicilioSGE.setDescVia(domicilioSW.getVia().getNombre());

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String descTipoViaPadron = domicilioSW.getVia().getDescTipoVia();
            datosTipoVia = fachadaSGE.getTipoViaByDescripcion(descTipoViaPadron, paramsConexionBD);
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setIdPais(domicilioSW.getCodPais());
        domicilioSGE.setIdProvincia(domicilioSW.getCodProvincia());
        domicilioSGE.setIdMunicipio(domicilioSW.getCodMunicipio());
        domicilioSGE.setIdPaisVia(domicilioSW.getVia().getCodPais());
        domicilioSGE.setIdProvinciaVia(domicilioSW.getVia().getCodProvincia());
        domicilioSGE.setIdMunicipioVia(domicilioSW.getVia().getCodMunicipio());

        domicilioSGE.setOrigen(nombreServicio);

        return domicilioSGE;
    }
}
