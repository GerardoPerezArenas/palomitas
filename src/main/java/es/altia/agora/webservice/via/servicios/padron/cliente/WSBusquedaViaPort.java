/**
 * WSBusquedaViaPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.via.servicios.padron.cliente;

public interface WSBusquedaViaPort extends java.rmi.Remote {
    public es.altia.agora.webservice.via.servicios.padron.cliente.DatosViaVO[] findVia(java.lang.String nombreVia, java.lang.String pais, java.lang.String provincia, java.lang.String municipio) throws java.rmi.RemoteException, es.altia.agora.webservice.via.servicios.padron.cliente.BusquedaViaWSException;
}
