/**
 * WSTramite.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion;

import es.altia.flexiaWS.tramitacion.*;
import es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO;
import es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO;

public interface WSTramitacionFlexia extends java.rmi.Remote {
    public es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean finalizarTramiteOperacion(ExpedienteVO idExpediente, TramiteVO idTramite, String origenLlamada, InfoConexionVO infoConexion) throws java.rmi.RemoteException;
    public es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO iniciarExpedienteOperacion(ExpedienteVO idExpediente, InfoConexionVO infoConexion) throws java.rmi.RemoteException;
}
