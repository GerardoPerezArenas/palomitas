/**
 * WSTramiteImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion;

public interface WSTramiteImpl extends java.rmi.Remote {
    public java.lang.String setTramite(es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.TramiteVO in0, java.lang.String in1) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.CROTramiteVO[] getTramiteExpediente(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBoolean finalizarTramiteResolucion(java.lang.String in0, boolean in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7, java.lang.String in8) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBoolean grabarCamposTramite(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBoolean finalizarConTramites(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBoolean grabarCampoGenerico(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7, java.lang.String in8) throws java.rmi.RemoteException;
}
