/**
 * WSBusquedaTerceroServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.padron.cliente;

public class WSBusquedaTerceroServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroService {

    public WSBusquedaTerceroServiceLocator() {
    }


    public WSBusquedaTerceroServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSBusquedaTerceroServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSBusquedaTerceroEndPoint
    private java.lang.String WSBusquedaTerceroEndPoint_address = "http://localhost:82/WSBusquedaTerceroPadron/services/WSBusquedaTerceroEndPoint";

    public java.lang.String getWSBusquedaTerceroEndPointAddress() {
        return WSBusquedaTerceroEndPoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSBusquedaTerceroEndPointWSDDServiceName = "WSBusquedaTerceroEndPoint";

    public java.lang.String getWSBusquedaTerceroEndPointWSDDServiceName() {
        return WSBusquedaTerceroEndPointWSDDServiceName;
    }

    public void setWSBusquedaTerceroEndPointWSDDServiceName(java.lang.String name) {
        WSBusquedaTerceroEndPointWSDDServiceName = name;
    }

    public es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroPort getWSBusquedaTerceroEndPoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSBusquedaTerceroEndPoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSBusquedaTerceroEndPoint(endpoint);
    }

    public es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroPort getWSBusquedaTerceroEndPoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroEndPointSoapBindingStub _stub = new es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroEndPointSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSBusquedaTerceroEndPointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSBusquedaTerceroEndPointEndpointAddress(java.lang.String address) {
        WSBusquedaTerceroEndPoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroPort.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroEndPointSoapBindingStub _stub = new es.altia.agora.webservice.tercero.servicios.padron.cliente.WSBusquedaTerceroEndPointSoapBindingStub(new java.net.URL(WSBusquedaTerceroEndPoint_address), this);
                _stub.setPortName(getWSBusquedaTerceroEndPointWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSBusquedaTerceroEndPoint".equals(inputPortName)) {
            return getWSBusquedaTerceroEndPoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tercero.ws.altia.es", "WSBusquedaTerceroService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tercero.ws.altia.es", "WSBusquedaTerceroEndPoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSBusquedaTerceroEndPoint".equals(portName)) {
            setWSBusquedaTerceroEndPointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
