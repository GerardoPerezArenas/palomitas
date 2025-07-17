/**
 * WSPistImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pist.cliente.servicio;

public class WSPistImplServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplService {

    public WSPistImplServiceLocator() {
    }


    public WSPistImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSPistImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSPistImpl
    private java.lang.String WSPistImpl_address = "http://localhost:82/SWPist/services/WSPistImpl";

    public java.lang.String getWSPistImplAddress() {
        return WSPistImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSPistImplWSDDServiceName = "WSPistImpl";

    public java.lang.String getWSPistImplWSDDServiceName() {
        return WSPistImplWSDDServiceName;
    }

    public void setWSPistImplWSDDServiceName(java.lang.String name) {
        WSPistImplWSDDServiceName = name;
    }

    public es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImpl getWSPistImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSPistImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSPistImpl(endpoint);
    }

    public es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImpl getWSPistImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplSoapBindingStub _stub = new es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSPistImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSPistImplEndpointAddress(java.lang.String address) {
        WSPistImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplSoapBindingStub _stub = new es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplSoapBindingStub(new java.net.URL(WSPistImpl_address), this);
                _stub.setPortName(getWSPistImplWSDDServiceName());
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
        if ("WSPistImpl".equals(inputPortName)) {
            return getWSPistImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "WSPistImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "WSPistImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSPistImpl".equals(portName)) {
            setWSPistImplEndpointAddress(address);
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
