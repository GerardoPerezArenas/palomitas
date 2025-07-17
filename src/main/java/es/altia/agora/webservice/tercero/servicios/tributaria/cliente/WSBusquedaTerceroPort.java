/**
 * WSBusquedaTerceroPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.tributaria.cliente;

public interface WSBusquedaTerceroPort extends java.rmi.Remote {
    public es.altia.agora.webservice.tercero.servicios.tributaria.cliente.DatosTerceroVO[] findTercero(java.lang.String doi, java.lang.String tipoDocumento, java.lang.String nombre, java.lang.String apellido1, java.lang.String apellido2) throws java.rmi.RemoteException, es.altia.agora.webservice.tercero.servicios.tributaria.cliente.BusquedaTerceroWSException;
    public es.altia.agora.webservice.tercero.servicios.tributaria.cliente.DatosDomicilioVO[] findDomicilio(java.lang.String idTercero, java.lang.String doi, java.lang.String tipoDocumento, java.lang.String nombre, java.lang.String apellido1, java.lang.String apellido2) throws java.rmi.RemoteException, es.altia.agora.webservice.tercero.servicios.tributaria.cliente.BusquedaTerceroWSException;
}
