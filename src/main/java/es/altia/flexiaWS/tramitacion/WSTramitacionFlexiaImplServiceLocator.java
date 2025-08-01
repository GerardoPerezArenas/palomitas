/**
 * WSTramitacionFlexiaImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion;

public class WSTramitacionFlexiaImplServiceLocator extends org.apache.axis.client.Service implements es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplService {

    public WSTramitacionFlexiaImplServiceLocator() {
    }


    public WSTramitacionFlexiaImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSTramitacionFlexiaImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSTramitacionFlexiaImpl
    private java.lang.String WSTramitacionFlexiaImpl_address = "http://localhost:8080/LCE_16.00_MAYO/services/WSTramitacionFlexiaImpl";

    public java.lang.String getWSTramitacionFlexiaImplAddress() {
        return WSTramitacionFlexiaImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSTramitacionFlexiaImplWSDDServiceName = "WSTramitacionFlexiaImpl";

    public java.lang.String getWSTramitacionFlexiaImplWSDDServiceName() {
        return WSTramitacionFlexiaImplWSDDServiceName;
    }

    public void setWSTramitacionFlexiaImplWSDDServiceName(java.lang.String name) {
        WSTramitacionFlexiaImplWSDDServiceName = name;
    }

    public es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImpl getWSTramitacionFlexiaImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSTramitacionFlexiaImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSTramitacionFlexiaImpl(endpoint);
    }

    public es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImpl getWSTramitacionFlexiaImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingStub _stub = new es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSTramitacionFlexiaImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSTramitacionFlexiaImplEndpointAddress(java.lang.String address) {
        WSTramitacionFlexiaImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingStub _stub = new es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingStub(new java.net.URL(WSTramitacionFlexiaImpl_address), this);
                _stub.setPortName(getWSTramitacionFlexiaImplWSDDServiceName());
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
        if ("WSTramitacionFlexiaImpl".equals(inputPortName)) {
            return getWSTramitacionFlexiaImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://servicios.flexia", "WSTramitacionFlexiaImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://servicios.flexia", "WSTramitacionFlexiaImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSTramitacionFlexiaImpl".equals(portName)) {
            setWSTramitacionFlexiaImplEndpointAddress(address);
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
