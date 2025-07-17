package es.altia.agora.webservice.tercero.servicios.nominas;

import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.servicios.nominas.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.servicios.nominas.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.nominas.cliente.DatosViaVO;
import es.altia.agora.webservice.tercero.servicios.nominas.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.nominas.cliente.WSBusquedaTerceroServiceLocator;
import java.util.Vector;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import javax.xml.rpc.ServiceException;
import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class BusquedaTerceroNominasImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroNominasImpl.class.getName());
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
            WSBusquedaTerceroServiceLocator terceroNominasLocator = new WSBusquedaTerceroServiceLocator();
            WSBusquedaTerceroPort terceroNominasService = terceroNominasLocator.getWSBusquedaTerceroEndPoint(urlEndPoint);
            DatosTerceroVO[] resultadoSW = terceroNominasService.findTercero(doi, tipoDocumento, nombre, apellido1, apellido2);
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
        domicilioSGE.setOrigen(nombreServicio);

        /*String domicilio = domicilioSW.getStrDireccion();
        domicilio = domicilio.replaceAll("\"", "");
        domicilioSGE.setDomicilio(domicilio);*/
        DatosViaVO via = domicilioSW.getVia();

        try {

            if (via.getCodTipoVia()!=null && !via.getCodTipoVia().equals(""))
                domicilioSGE.setIdTipoVia(via.getCodTipoVia());
            
            if (via.getNombre()!=null && !via.getNombre().equals(""))
                domicilioSGE.setDescVia(via.getNombre());

            if (domicilioSW.getCodPais() != null && !"".equals(domicilioSW.getCodPais())) {
                String codPais = domicilioSW.getCodPais();
                domicilioSGE.setIdPais(codPais);
                String descPais =
                        (String) fachadaSGE.getPaisByCodigo(Integer.parseInt(codPais), paramsConexionBD).getAtributo("nombrePais");
                domicilioSGE.setPais(descPais);


                if (!codPais.equals("108")) {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                    domicilioSGE.setIdPais("108");
                    domicilioSGE.setPais("ESPAÑA");
                    domicilioSGE.setIdProvincia("66");
                    domicilioSGE.setProvincia("EXTRANJERO");
                    domicilioSGE.setIdMunicipio(codPais);
                    domicilioSGE.setMunicipio(descPais);
                    domicilioSGE.setIdPaisVia(codPais);
                    domicilioSGE.setIdProvinciaVia("66");
                    domicilioSGE.setIdMunicipioVia(codPais);

                } else {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                    GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndDesc(Integer.parseInt(domicilioSGE.getIdPais()),
                            domicilioSW.getDescProvincia(), paramsConexionBD);

                    String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codProvincia);
                    String descProvincia = (String) infoProvincia.getAtributo("nombreProvincia");
                    domicilioSGE.setProvincia(descProvincia);

                    MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndDesc(Integer.parseInt(domicilioSGE.getIdPais()),
                            Integer.parseInt(domicilioSGE.getIdProvincia()), domicilioSW.getDescMunicipio(), paramsConexionBD);

                    domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                    domicilioSGE.setMunicipio(infoMunicipio.getNombreOficial());
                    domicilioSGE.setIdPaisVia(codPais);
                    domicilioSGE.setIdProvinciaVia(codProvincia);
                    domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));

                }
            } else {
                m_Log.debug("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setPais("ESPAÑA");
                domicilioSGE.setIdProvincia("99");
                domicilioSGE.setProvincia("DESCONOCIDA");
                domicilioSGE.setIdMunicipio("999");
                domicilioSGE.setMunicipio("DESCONOCIDO");

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
            
        }

        return domicilioSGE;
    }

}
