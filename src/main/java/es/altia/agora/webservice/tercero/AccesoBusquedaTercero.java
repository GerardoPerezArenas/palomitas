package es.altia.agora.webservice.tercero;

import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.webservice.tercero.exception.InstanciacionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.exception.DemasiadosResultadosBusquedaTerceroException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AccesoBusquedaTercero {

    private Vector<String> idsServicios;
    private String prefijoPropiedad;

    protected static Log log = LogFactory.getLog(AccesoBusquedaTercero.class.getName());

    public AccesoBusquedaTercero(Vector<String> idsServicios, String prefijoOrg) {
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

    public HashMap getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) {

        Vector tercerosEncontrados = new Vector();
        Collection<String> errores = new ArrayList<String>();

        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");

        /*
        * Recorremos el vector de identificadores. Recuperamos la implementacion correspondiente de
        * FachadaBusquedaTercero a partir del identificador. Ejecutamos el metodo getTercero de la implementacion
        * para recuperar los terceros de ese servicio.
        */
        for (String idServicio : idsServicios) {

            try {
                FachadaBusquedaTercero fachadaBusqueda = FactoriaBusquedaTercero.getImpl(idServicio, prefijoPropiedad);
                String strContinuar = m_ConfigTerceros.getString(fachadaBusqueda.getPrefijoPropiedad() + "continuar");
                boolean continuar = Boolean.parseBoolean(strContinuar);

                Vector tercerosServicio = fachadaBusqueda.getTercero(condsBusqueda, params);
                tercerosEncontrados.addAll(tercerosServicio);
                if (tercerosEncontrados.size() > 0 && !continuar) {
                    return buildEstructuraRetorno(tercerosEncontrados, errores);
                }

            } catch (InstanciacionBusquedaTerceroException ibte) {
                log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
            } catch (EjecucionBusquedaTerceroException ebte) {
                log.error("SE HA PRODUCIDO UN ERROR AL EJECUTAR EL METODO DE BUSQUEDA DEL SERVICIO " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");
                errores.add(idServicio);
             } catch ( DemasiadosResultadosBusquedaTerceroException dvex) {
                log.error("SE HAN OBTENIDO DEMASIADOS RESULTADOS. SE DEBE REFINAR LA BÚSQUEDA " + idServicio);
                log.debug("CONTINUAMOS CON EL SIGUIENTE SERVICIO DE LA LISTA");               
                errores.add("DEMASIADOS_RESULTADOS");  
                 return buildEstructuraRetorno(tercerosEncontrados, errores);
            }

        }

        return buildEstructuraRetorno(tercerosEncontrados, errores);
    }

    private HashMap buildEstructuraRetorno(Vector terceros, Collection<String> errores) {
        HashMap estructuraRetorno = new HashMap();
        estructuraRetorno.put("resultados", terceros);
        estructuraRetorno.put("errores", errores);
        return estructuraRetorno;
    }
}
