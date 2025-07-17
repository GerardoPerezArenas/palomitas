/**
 * SWPisaService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.pisa.cliente;

public interface SWPisaService extends javax.xml.rpc.Service {
    public java.lang.String getPisaAddress();

    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa getPisa() throws javax.xml.rpc.ServiceException;

    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa getPisa(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
