package es.altia.agora.webservice.via;

import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.webservice.via.exception.EjecucionBusquedaViaException;

import java.util.Collection;

public interface FachadaBusquedaVia {

    public String getNombreServicio();

    public void setNombreServicio(String nombreServicio);

    public String getPrefijoPropiedad();

    public void setPrefijoPropiedad(String prefijoPropiedad);

    public Collection<ViaEncontradaVO> buscarVias(CondicionesBusquedaViaVO conds, String[] params) throws EjecucionBusquedaViaException;
}
