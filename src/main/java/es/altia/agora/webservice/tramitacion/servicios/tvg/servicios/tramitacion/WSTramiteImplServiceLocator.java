/**
 * WSTramiteImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion;

public class WSTramiteImplServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramiteImplService {

    public WSTramiteImplServiceLocator() {
    }


    public WSTramiteImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSTramiteImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSTramiteImpl
    private java.lang.String WSTramiteImpl_address = "http://localhost:8080/sigp/services/WSTramiteImpl";

    public java.lang.String getWSTramiteImplAddress() {
        return WSTramiteImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSTramiteImplWSDDServiceName = "WSTramiteImpl";

    public java.lang.String getWSTramiteImplWSDDServiceName() {
        return WSTramiteImplWSDDServiceName;
    }

    public void setWSTramiteImplWSDDServiceName(java.lang.String name) {
        WSTramiteImplWSDDServiceName = name;
    }

    public es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramiteImpl getWSTramiteImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSTramiteImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSTramiteImpl(endpoint);
    }

    public es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramiteImpl getWSTramiteImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramitacionImplSoapBindingStub _stub = new es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramitacionImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSTramiteImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSTramiteImplEndpointAddress(java.lang.String address) {
        WSTramiteImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramiteImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramitacionImplSoapBindingStub _stub = new es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion.WSTramitacionImplSoapBindingStub(new java.net.URL(WSTramiteImpl_address), this);
                _stub.setPortName(getWSTramiteImplWSDDServiceName());
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
        if ("WSTramiteImpl".equals(inputPortName)) {
            return getWSTramiteImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://servicios.tvg", "WSTramiteImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://servicios.tvg", "WSTramiteImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSTramiteImpl".equals(portName)) {
            setWSTramiteImplEndpointAddress(address);
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
