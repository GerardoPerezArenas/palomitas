/**
 * WSTramitacionFlexiaImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion;

public interface WSTramitacionFlexiaImpl extends java.rmi.Remote {
    public es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean finalizarTramiteOperacion(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO in0, es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO in1, es.altia.flexiaWS.tramitacion.bd.datos.CondicionFinalizacionVO in2, java.lang.String in3, es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO in4) throws java.rmi.RemoteException;
    public es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO iniciarExpedienteOperacion(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO in0, es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException;
}
