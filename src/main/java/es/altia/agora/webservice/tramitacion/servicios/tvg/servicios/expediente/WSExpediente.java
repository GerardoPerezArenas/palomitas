package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente;

import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.ExpedienteVO;

/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 30-may-2006
 * Time: 16:50:18
 * To change this template use File | Settings | File Templates.
 */
public interface WSExpediente {

    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBooleanExpediente setExpediente(ExpedienteVO expediente, String jndi);
}
