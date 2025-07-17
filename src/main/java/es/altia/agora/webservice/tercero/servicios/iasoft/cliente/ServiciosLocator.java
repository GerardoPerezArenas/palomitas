/**
 * ServiciosLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.iasoft.cliente;

public class ServiciosLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.tercero.servicios.iasoft.cliente.Servicios {

    public ServiciosLocator() {
    }


    public ServiciosLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServiciosLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServiciosSoap
    private java.lang.String ServiciosSoap_address = "http://cambiodomicilio.demos.iasoft.es/Servicios.asmx";

    public java.lang.String getServiciosSoapAddress() {
        return ServiciosSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServiciosSoapWSDDServiceName = "ServiciosSoap";

    public java.lang.String getServiciosSoapWSDDServiceName() {
        return ServiciosSoapWSDDServiceName;
    }

    public void setServiciosSoapWSDDServiceName(java.lang.String name) {
        ServiciosSoapWSDDServiceName = name;
    }

    public es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoap getServiciosSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServiciosSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServiciosSoap(endpoint);
    }

    public es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoap getServiciosSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoapStub _stub = new es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoapStub(portAddress, this);
            _stub.setPortName(getServiciosSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServiciosSoapEndpointAddress(java.lang.String address) {
        ServiciosSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoapStub _stub = new es.altia.agora.webservice.tercero.servicios.iasoft.cliente.ServiciosSoapStub(new java.net.URL(ServiciosSoap_address), this);
                _stub.setPortName(getServiciosSoapWSDDServiceName());
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
        if ("ServiciosSoap".equals(inputPortName)) {
            return getServiciosSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.red.es/padron", "Servicios");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.red.es/padron", "ServiciosSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServiciosSoap".equals(portName)) {
            setServiciosSoapEndpointAddress(address);
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
