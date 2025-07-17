/**
 * WSDocumentosFlexiaImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos;

public class WSDocumentosFlexiaImplServiceLocator extends org.apache.axis.client.Service implements es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplService {

    public WSDocumentosFlexiaImplServiceLocator() {
    }


    public WSDocumentosFlexiaImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSDocumentosFlexiaImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSDocumentosFlexiaImpl
    private java.lang.String WSDocumentosFlexiaImpl_address = "http://localhost:8090/Flexia18.03_Lanbide/services/WSDocumentosFlexiaImpl";

    public java.lang.String getWSDocumentosFlexiaImplAddress() {
        return WSDocumentosFlexiaImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSDocumentosFlexiaImplWSDDServiceName = "WSDocumentosFlexiaImpl";

    public java.lang.String getWSDocumentosFlexiaImplWSDDServiceName() {
        return WSDocumentosFlexiaImplWSDDServiceName;
    }

    public void setWSDocumentosFlexiaImplWSDDServiceName(java.lang.String name) {
        WSDocumentosFlexiaImplWSDDServiceName = name;
    }

    public es.altia.flexiaWS.documentos.WSDocumentosFlexiaImpl getWSDocumentosFlexiaImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSDocumentosFlexiaImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSDocumentosFlexiaImpl(endpoint);
    }

    public es.altia.flexiaWS.documentos.WSDocumentosFlexiaImpl getWSDocumentosFlexiaImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplSoapBindingStub _stub = new es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSDocumentosFlexiaImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSDocumentosFlexiaImplEndpointAddress(java.lang.String address) {
        WSDocumentosFlexiaImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.flexiaWS.documentos.WSDocumentosFlexiaImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplSoapBindingStub _stub = new es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplSoapBindingStub(new java.net.URL(WSDocumentosFlexiaImpl_address), this);
                _stub.setPortName(getWSDocumentosFlexiaImplWSDDServiceName());
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
        if ("WSDocumentosFlexiaImpl".equals(inputPortName)) {
            return getWSDocumentosFlexiaImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://servicios.flexia", "WSDocumentosFlexiaImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://servicios.flexia", "WSDocumentosFlexiaImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSDocumentosFlexiaImpl".equals(portName)) {
            setWSDocumentosFlexiaImplEndpointAddress(address);
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
