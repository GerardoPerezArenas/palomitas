package es.altia.agora.webservice.via;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.webservice.via.exception.EjecucionBusquedaViaException;
import es.altia.agora.webservice.via.exception.InstanciacionBusquedaViaException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class AccesoBusquedaVia {

    private Vector<String> idsServicios;
    private String prefijoPropiedad;

    protected static Log log = LogFactory.getLog(AccesoBusquedaVia.class.getName());

    public AccesoBusquedaVia(Vector<String> idsServicios, String prefijoOrg) {
        this.idsServicios = idsServicios;
        this.prefijoPropiedad = prefijoOrg;
    }

    public Vector getIdsServicios() {
        return idsServicios;
    }

    public void setIdsServicios(Vector<String> idsServicios) {
        this.idsServicios = idsServicios;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public HashMap<String, Collection> buscarVias(CondicionesBusquedaViaVO condsBusqueda, String[] params) {

        Collection<ViaEncontradaVO> viasEncontradas = new ArrayList<ViaEncontradaVO>();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigVias = ConfigServiceHelper.getConfig("Vias");

        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * FachadaBusquedaTercero a partir del identificador. Ejecutamos el metodo buscarVias de la implementacion
        * para recuperar las vias de ese servicio.
        */
        for (String idServicio : idsServicios) {

            try {
                FachadaBusquedaVia fachadaBusqueda = FactoriaBusquedaVia.getImpl(idServicio, prefijoPropiedad);
                String strContinuar = m_ConfigVias.getString(fachadaBusqueda.getPrefijoPropiedad() + "continuar");
                boolean continuar = Boolean.parseBoolean(strContinuar);

                Collection<ViaEncontradaVO> viasServicio = fachadaBusqueda.buscarVias(condsBusqueda, params);
                viasEncontradas.addAll(viasServicio);
                if (viasServicio.size() > 0 && !continuar) return buildEstructuraRetorno(viasEncontradas, errores);

            } catch (InstanciacionBusquedaViaException ibte) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (EjecucionBusquedaViaException ebve) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            }

        }

        return buildEstructuraRetorno(viasEncontradas, errores);
    }

    private HashMap<String, Collection> buildEstructuraRetorno(Collection<ViaEncontradaVO> vias, Collection<String> errores) {
        HashMap<String, Collection> estructuraRetorno = new HashMap<String, Collection>();
        estructuraRetorno.put("resultados", vias);
        estructuraRetorno.put("errores", errores);
        return estructuraRetorno;
    }
}
