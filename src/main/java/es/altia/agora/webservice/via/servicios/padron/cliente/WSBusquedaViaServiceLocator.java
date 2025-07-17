/**
 * WSBusquedaViaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.via.servicios.padron.cliente;

public class WSBusquedaViaServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaService {

    public WSBusquedaViaServiceLocator() {
    }


    public WSBusquedaViaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSBusquedaViaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSBusquedaViaEndPoint
    private java.lang.String WSBusquedaViaEndPoint_address = "http://localhost:82/WSBusquedaViaPadron/services/WSBusquedaViaEndPoint";

    public java.lang.String getWSBusquedaViaEndPointAddress() {
        return WSBusquedaViaEndPoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSBusquedaViaEndPointWSDDServiceName = "WSBusquedaViaEndPoint";

    public java.lang.String getWSBusquedaViaEndPointWSDDServiceName() {
        return WSBusquedaViaEndPointWSDDServiceName;
    }

    public void setWSBusquedaViaEndPointWSDDServiceName(java.lang.String name) {
        WSBusquedaViaEndPointWSDDServiceName = name;
    }

    public es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaPort getWSBusquedaViaEndPoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSBusquedaViaEndPoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSBusquedaViaEndPoint(endpoint);
    }

    public es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaPort getWSBusquedaViaEndPoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaEndPointSoapBindingStub _stub = new es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaEndPointSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSBusquedaViaEndPointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSBusquedaViaEndPointEndpointAddress(java.lang.String address) {
        WSBusquedaViaEndPoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaPort.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaEndPointSoapBindingStub _stub = new es.altia.agora.webservice.via.servicios.padron.cliente.WSBusquedaViaEndPointSoapBindingStub(new java.net.URL(WSBusquedaViaEndPoint_address), this);
                _stub.setPortName(getWSBusquedaViaEndPointWSDDServiceName());
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
        if ("WSBusquedaViaEndPoint".equals(inputPortName)) {
            return getWSBusquedaViaEndPoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://via.ws.altia.es", "WSBusquedaViaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://via.ws.altia.es", "WSBusquedaViaEndPoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSBusquedaViaEndPoint".equals(portName)) {
            setWSBusquedaViaEndPointEndpointAddress(address);
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
