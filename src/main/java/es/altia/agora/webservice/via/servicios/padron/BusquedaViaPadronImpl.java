package es.altia.agora.webservice.via.servicios.padron;

import es.altia.agora.webservice.via.FachadaBusquedaVia;
import es.altia.agora.webservice.via.FachadaSGEVia;
import es.altia.agora.webservice.via.servicios.padron.cliente.DatosViaVO;
import es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaPort;
import es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaServiceLocator;
import es.altia.agora.webservice.via.exception.EjecucionBusquedaViaException;
import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TiposViasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.Collection;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;

public class BusquedaViaPadronImpl implements FachadaBusquedaVia {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaViaPadronImpl.class.getName());
    private FachadaSGEVia fachadaSGE = new FachadaSGEVia();

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

    public Collection<ViaEncontradaVO> buscarVias(CondicionesBusquedaViaVO conds, String[] params)
    throws EjecucionBusquedaViaException {

        try {
            paramsConexionBD = params;
            Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Vias");

            String strUrlEndPoint = m_ConfigTerceros.getString(prefijoPropiedad + "urlEndPoint");

            String nombreVia = conds.getNombreVia();
            String pais = Integer.toString(conds.getCodPais());
            String provincia = Integer.toString(conds.getCodProvincia());
            String municipio = Integer.toString(conds.getCodMunicipio());

            URL urlEndPoint = new URL(strUrlEndPoint);
            WSBusquedaViaServiceLocator viaPadronLocator = new WSBusquedaViaServiceLocator();
            WSBusquedaViaPort viaPadronService = viaPadronLocator.getWSBusquedaViaEndPoint(urlEndPoint);
            DatosViaVO[] resultadoSW = viaPadronService.findVia(nombreVia, pais, provincia, municipio);
            return transformarViaArraySW(resultadoSW);

        } catch (ServiceException se) {
            throw new EjecucionBusquedaViaException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", se);
        } catch (RemoteException re) {
            throw new EjecucionBusquedaViaException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", re);
        } catch (MalformedURLException mue) {
            throw new EjecucionBusquedaViaException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mue);
        } catch (MissingResourceException mre) {
            throw new EjecucionBusquedaViaException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mre);
        }


    }

    private Collection<ViaEncontradaVO> transformarViaArraySW(DatosViaVO[] resultadoSW) {

        Collection<ViaEncontradaVO> arrayViaSGE = new ArrayList<ViaEncontradaVO>();
        for (DatosViaVO viaSW: resultadoSW) {
            arrayViaSGE.add(transformarViaSW(viaSW));
        }
        return arrayViaSGE;        
    }

    private ViaEncontradaVO transformarViaSW(DatosViaVO viaSW) {

        ViaEncontradaVO viaSGE = new ViaEncontradaVO();
        viaSGE.setCodigoVia(0);
        viaSGE.setCodigoPais(Integer.parseInt(viaSW.getCodPais()));
        viaSGE.setDescPais(viaSW.getDescPais());
        viaSGE.setCodigoProvincia(Integer.parseInt(viaSW.getCodProvincia()));
        viaSGE.setDescProvincia(viaSW.getDescProvincia());
        viaSGE.setCodigoMunicipio(Integer.parseInt(viaSW.getCodMunicipio()));
        viaSGE.setDescMunicipio(viaSW.getDescMunicipio());
        viaSGE.setNombreCortoVia(viaSW.getNombre().toUpperCase());
        viaSGE.setNombreVia(viaSW.getNombre().toUpperCase());


        viaSGE.setDescTipoVia(viaSW.getDescTipoVia());

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String descTipoViaPadron = viaSW.getDescTipoVia().toUpperCase();
            datosTipoVia = fachadaSGE.getTipoViaByDescripcion(descTipoViaPadron, paramsConexionBD);

            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                String abrTipoViaPadron = viaSW.getCodTipoVia().toUpperCase();
                datosTipoVia = TiposViasDAO.getInstance().getTipoViaByAbreviatura(abrTipoViaPadron, paramsConexionBD);
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        viaSGE.setCodigoTipoVia(Integer.parseInt((String)datosTipoVia.getAtributo("codTipoVia")));
        viaSGE.setDescTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        viaSGE.setInfoTramero(null);

        return viaSGE;
    }
}
