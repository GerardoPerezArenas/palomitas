package es.altia.agora.webservice.via.servicios.sge;

import es.altia.agora.webservice.via.FachadaBusquedaVia;
import es.altia.agora.webservice.via.exception.EjecucionBusquedaViaException;
import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ViasDAO;
import es.altia.common.exception.TechnicalException;

import java.util.Collection;

public class BusquedaViaSGEImpl implements FachadaBusquedaVia {

    private String nombreServicio;
    private String prefijoPropiedad;

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

    public Collection<ViaEncontradaVO> buscarVias(CondicionesBusquedaViaVO conds, String[] params)
    throws EjecucionBusquedaViaException {

        try {
            ViasDAO viasDAO = ViasDAO.getInstance();
            return viasDAO.getViasByCondiciones(conds, params);
        } catch (TechnicalException te) {
            throw new EjecucionBusquedaViaException("ERROR EN LA EJECUCION DE LA LLAMADA AL SERVICIO DE " +
                    "BUSQUEDA " + this.nombreServicio, te);
        }
    }
}
