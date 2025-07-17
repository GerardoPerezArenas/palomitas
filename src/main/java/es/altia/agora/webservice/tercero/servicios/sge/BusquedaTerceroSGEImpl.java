package es.altia.agora.webservice.tercero.servicios.sge;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.exception.DemasiadosResultadosBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.common.exception.TechnicalException;

import java.util.Vector;

public class BusquedaTerceroSGEImpl implements FachadaBusquedaTercero {

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

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException, DemasiadosResultadosBusquedaTerceroException {

        try {
            TercerosDAO tercerosDAO = TercerosDAO.getInstance();
            return tercerosDAO.getTercero(condsBusqueda, params);
            
        
        }  catch (DemasiadosResultadosBusquedaTerceroException de) {
            throw new DemasiadosResultadosBusquedaTerceroException("DEMASIADOS VALORES", de);
      
        } catch (TechnicalException te) {
            throw new EjecucionBusquedaTerceroException("ERROR EN LA EJECUCION DE LA LLAMADA AL SERVICIO DE " +
                    "BUSQUEDA " + this.nombreServicio, te);            
        }
    }
}
