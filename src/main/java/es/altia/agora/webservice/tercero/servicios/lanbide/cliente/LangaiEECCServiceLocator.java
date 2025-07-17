/**
 * LangaiEECCServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.lanbide.cliente;

public class LangaiEECCServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCService {

    public LangaiEECCServiceLocator() {
    }


    public LangaiEECCServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LangaiEECCServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LangaiEECC
    private java.lang.String LangaiEECC_address = "http://10.168.212.21:17003/langaiWS/services/LangaiEECC";

    public java.lang.String getLangaiEECCAddress() {
        return LangaiEECC_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LangaiEECCWSDDServiceName = "LangaiEECC";

    public java.lang.String getLangaiEECCWSDDServiceName() {
        return LangaiEECCWSDDServiceName;
    }

    public void setLangaiEECCWSDDServiceName(java.lang.String name) {
        LangaiEECCWSDDServiceName = name;
    }

    public es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECC getLangaiEECC() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LangaiEECC_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLangaiEECC(endpoint);
    }

    public es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECC getLangaiEECC(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCSoapBindingStub _stub = new es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCSoapBindingStub(portAddress, this);
            _stub.setPortName(getLangaiEECCWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLangaiEECCEndpointAddress(java.lang.String address) {
        LangaiEECC_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECC.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCSoapBindingStub _stub = new es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCSoapBindingStub(new java.net.URL(LangaiEECC_address), this);
                _stub.setPortName(getLangaiEECCWSDDServiceName());
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
        if ("LangaiEECC".equals(inputPortName)) {
            return getLangaiEECC();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:LangaiEECC", "LangaiEECCService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:LangaiEECC", "LangaiEECC"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LangaiEECC".equals(portName)) {
            setLangaiEECCEndpointAddress(address);
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
