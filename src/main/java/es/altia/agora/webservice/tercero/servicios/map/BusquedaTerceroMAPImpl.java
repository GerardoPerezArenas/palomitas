package es.altia.agora.webservice.tercero.servicios.map;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.map.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.map.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.map.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.map.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;

public class BusquedaTerceroMAPImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroMAPImpl.class.getName());
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
            WSBusquedaTerceroServiceLocator terceroMapLocator = new WSBusquedaTerceroServiceLocator();
            WSBusquedaTerceroPort terceroMapService = terceroMapLocator.getWSBusquedaTerceroEndPoint(urlEndPoint);
            m_Log.debug("BusquedaTerceroMAPImpl antes de llamar a findTercero");
            DatosTerceroVO[] resultadoSW = terceroMapService.findTercero(doi, tipoDocumento, nombre, apellido1, apellido2);

            m_Log.debug("BusquedaTerceroMAPImpl después de llamar a findTercero");

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
        m_Log.debug("transformaTerceroArraySW");
        Vector<TercerosValueObject> arrayTerceroSGE = new Vector<TercerosValueObject>();
        for (DatosTerceroVO terceroSW: resultadoSW) {
            arrayTerceroSGE.add(transformarTerceroSW(terceroSW));
        }

        m_Log.debug("transformaTerceroArraySW nº terceros: " + arrayTerceroSGE.size());
        return arrayTerceroSGE;
    }

    private TercerosValueObject transformarTerceroSW(DatosTerceroVO terceroSW) {
        m_Log.debug("transformarTerceroSW");
        Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
        String eliminarTildes = m_ConfigCommon.getString("eliminar_tildes_terceros_map");
        boolean quitarTildes = false;
        if("true".equals(eliminarTildes))
            quitarTildes = true;

        TercerosValueObject terceroSGE = new TercerosValueObject();
        m_Log.debug("transformarTerceroSW nombre: " + terceroSW.getNombre());
        if(quitarTildes){
            terceroSGE.setNombre(terceroSW.getNombre().toUpperCase().replaceAll("Á","A").replaceAll("É","E").replaceAll("Í","I").replaceAll("Ó","O").replaceAll("Ú","U"));
            terceroSGE.setNombre(terceroSW.getNombre().toUpperCase().replaceAll("À","A").replaceAll("È","E").replaceAll("Ì","I").replaceAll("Ò","O").replaceAll("Ù","U"));
            terceroSGE.setNombre(terceroSW.getNombre().toUpperCase().replaceAll("Ä","A").replaceAll("Ë","E").replaceAll("Ï","I").replaceAll("Ö","O").replaceAll("Ü","U"));
        }
        else
        terceroSGE.setNombre(terceroSW.getNombre().toUpperCase());

        // Los nombres de terceros traidos del MAP cuya longitud sea superior a 82, se recortan
        // porque no se pueden guardar en la tabla T_TER
        m_Log.debug("************ BusquedaTerceroMAPImpl. transformarTerceroSW longitud : " + terceroSW.getNombre().length());
        if(terceroSW.getNombre()!=null && terceroSW.getNombre().length()>=82){
            terceroSGE.setNombre((terceroSGE.getNombre().substring(0,75) + "...").toUpperCase());
            //terceroSGE.setNombre(terceroSGE.getNombre().substring(0,75));
            m_Log.debug("************ BusquedaTerceroMAPImpl. transformarTerceroSW() - nombre tercero transformado : " + terceroSW.getNombre());
        }
        else
            m_Log.debug("************ BusquedaTerceroMAPImpl. transformarTerceroSW() - No se transforma el nombre del tercero");

        terceroSGE.setIdentificador("0");

        terceroSGE.setTelefono(terceroSW.getTelefono());
        m_Log.debug("transformarTerceroSW telefono: " + terceroSW.getTelefono());
        terceroSGE.setTipoDocumento(terceroSW.getTipoDocumento());
        m_Log.debug("transformarTerceroSW tipoDocumento: " + terceroSW.getTipoDocumento());
        terceroSGE.setDocumento(terceroSW.getDoi());
        m_Log.debug("transformarTerceroSW doi: " + terceroSW.getDoi());
        terceroSGE.setOrigen("MAP");
        terceroSGE.setDomicilios(transformarDomicilioArraySW(terceroSW.getDomicilios()));

        terceroSGE.setSituacion('A');

        return terceroSGE;
    }

    private Vector<DomicilioSimpleValueObject> transformarDomicilioArraySW(DatosDomicilioVO[] resultadoSW) {
        m_Log.debug("transformarDomicilioArraySW ");
        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();
        for (DatosDomicilioVO domicilioSW: resultadoSW) {
            m_Log.debug("transformarDomicilioArraySW iterando");
            arrayDomicilioSGE.add(transformarDomicilioSW(domicilioSW));
        }
        return arrayDomicilioSGE;
    }

    /**
     * Reemplaza en una cadena una determinada cadena de caracteres por otra
     * @param cadena: Cadena en la que se hace la sustitución
     * @param buscar: Cadena a reemplazar
     * @param sustituto: Cadena por la que se hace la sustitución
     */
    private String replace(String cadena,String buscar,String sustituto)
    {
        String salida = "";
        if(cadena!=null && buscar!=null && sustituto!=null){
            salida = cadena.replaceAll(buscar.trim(),sustituto.trim());
        }

        m_Log.debug("cadena reemplazada: " + salida);
        return salida;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(DatosDomicilioVO domicilioSW) {

        Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
        String eliminarTildes = m_ConfigCommon.getString("eliminar_tildes_terceros_map");
        m_Log.debug("BusquedaTercerosMAPImpl valor eliminarTildes: " + eliminarTildes);
        boolean quitarTildes = false;
        if("true".equals(eliminarTildes))
            quitarTildes = true;

        m_Log.debug("transformarDomicilioSW entrando");
        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(replace(domicilioSW.getCodPostal(),"\"","'"));
        m_Log.debug("transformarDomicilioSW codigoPostal: " + replace(domicilioSW.getCodPostal(),"\"","'"));

        if(quitarTildes){
            domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio().replaceAll("Á","A").replaceAll("É","E").replaceAll("Í","I").replaceAll("Ó","O").replaceAll("Ú","U"));
            domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio().replaceAll("À","A").replaceAll("È","E").replaceAll("Ì","I").replaceAll("Ò","O").replaceAll("Ù","U"));
            domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio().replaceAll("Ä","A").replaceAll("Ë","E").replaceAll("Ï","I").replaceAll("Ö","O").replaceAll("Ü","U"));
        }
        else
        domicilioSGE.setMunicipio(domicilioSW.getDescMunicipio());

        m_Log.debug("transformarDomicilioSW municipio: " + replace(domicilioSW.getDescMunicipio(),"\"","'"));
        if(quitarTildes){
            domicilioSGE.setProvincia(domicilioSW.getDescProvincia().replaceAll("Á","A").replaceAll("É","E").replaceAll("Í","I").replaceAll("Ó","O").replaceAll("Ú","U"));
            domicilioSGE.setProvincia(domicilioSW.getDescProvincia().replaceAll("À","A").replaceAll("È","E").replaceAll("Ì","I").replaceAll("Ò","O").replaceAll("Ù","U"));
            domicilioSGE.setProvincia(domicilioSW.getDescProvincia().replaceAll("Ä","A").replaceAll("Ë","E").replaceAll("Ï","I").replaceAll("Ö","O").replaceAll("Ü","U"));
        }
        else
        domicilioSGE.setProvincia(domicilioSW.getDescProvincia());

        m_Log.debug("transformarDomicilioSW provincia: " + replace(domicilioSW.getDescProvincia(),"\"","'"));
        if(quitarTildes){
            domicilioSGE.setPais(replace(domicilioSW.getDescPais(),"\"","'").replaceAll("Á","A").replaceAll("É","E").replaceAll("Í","I").replaceAll("Ó","O").replaceAll("Ú","U"));
            domicilioSGE.setPais(replace(domicilioSW.getDescPais(),"\"","'").replaceAll("À","A").replaceAll("È","E").replaceAll("Ì","I").replaceAll("Ò","O").replaceAll("Ù","U"));
            domicilioSGE.setPais(replace(domicilioSW.getDescPais(),"\"","'").replaceAll("Ä","A").replaceAll("Ë","E").replaceAll("Ï","I").replaceAll("Ö","O").replaceAll("Ü","U"));
        }
        else
        domicilioSGE.setPais(replace(domicilioSW.getDescPais(),"\"","'"));

        m_Log.debug("transformarDomicilioSW pais: " + replace(domicilioSW.getDescPais(),"\"","'"));
        domicilioSGE.setIdDomicilio("0");
        if(quitarTildes){
            domicilioSGE.setDomicilio(replace(domicilioSW.getStrDireccion(),"\"","'").toUpperCase().replaceAll("Á","A").replaceAll("É","E").replaceAll("Í","I").replaceAll("Ó","O").replaceAll("Ú","U"));
            domicilioSGE.setDomicilio(replace(domicilioSW.getStrDireccion(),"\"","'").toUpperCase().replaceAll("À","A").replaceAll("È","E").replaceAll("Ì","I").replaceAll("Ò","O").replaceAll("Ù","U"));
            domicilioSGE.setDomicilio(replace(domicilioSW.getStrDireccion(),"\"","'").toUpperCase().replaceAll("Ä","A").replaceAll("Ë","E").replaceAll("Ï","I").replaceAll("Ö","O").replaceAll("Ü","U"));
        }
        else
        domicilioSGE.setDomicilio(replace(domicilioSW.getStrDireccion(),"\"","'").toUpperCase());

        m_Log.debug("transformarDomicilioSW direccion: " + replace(domicilioSW.getStrDireccion(),"\"","'").toUpperCase());

        try {
            GeneralValueObject infoPais = fachadaSGE.getPaisByDescription(domicilioSW.getDescPais(), paramsConexionBD);
            String codPais = (String)infoPais.getAtributo("codigoPais");
            domicilioSGE.setIdPais(codPais);
            domicilioSGE.setIdPaisVia(codPais);

            m_Log.debug("transformarDomicilioSW codPais resultado de getPaisDescription(): " + codPais);

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
                GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndDesc(
                            Integer.parseInt(codPais), domicilioSW.getDescProvincia(), paramsConexionBD);

                String codProvincia = (String)infoProvincia.getAtributo("codigoProvincia");
                domicilioSGE.setIdProvincia(codProvincia);
                domicilioSGE.setIdProvinciaVia(codProvincia);

                m_Log.debug("transformaDomicilioSW codPais==108 codPais : " + codPais);
                m_Log.debug("transformaDomicilioSW codPais==108 codProvincia : " + codProvincia);
                if(codPais!=null && codProvincia!=null && codPais.length()>0 && codProvincia.length()>0){
                    m_Log.debug("transformaDomicilioSW ANTES DE LLAMAR A getMunicipioByPaisAndProvAndDesc()");
                MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndDesc(Integer.parseInt(codPais),
                        Integer.parseInt(codProvincia), domicilioSW.getDescMunicipio(), paramsConexionBD);

                    if(infoMunicipio!=null)
                        m_Log.debug("transformaDomicilioSW infoMunicipio!=null codigoMunicipio: " + infoMunicipio.getCodigoMunicipio());
                    else
                        m_Log.debug("transformaDomicilioSW infoMunicipio==null");

                domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));
            }
            }// else

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
