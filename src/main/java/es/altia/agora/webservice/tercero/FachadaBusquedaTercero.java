package es.altia.agora.webservice.tercero;

import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.webservice.tercero.exception.DemasiadosResultadosBusquedaTerceroException;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;

import java.util.Vector;

public interface FachadaBusquedaTercero {

    public String getNombreServicio();
    
    public void setNombreServicio(String nombreServicio);
    
    public String getPrefijoPropiedad();

    public void setPrefijoPropiedad(String prefijoPropiedad);

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException, DemasiadosResultadosBusquedaTerceroException;

}
