/**
 * SWPisaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pisa.cliente.servicio;

public class SWPisaServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisaService {

    public SWPisaServiceLocator() {
    }


    public SWPisaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SWPisaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Pisa
    private java.lang.String Pisa_address = "http://neptuno:8080/services/Pisa";

    public java.lang.String getPisaAddress() {
        return Pisa_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PisaWSDDServiceName = "Pisa";

    public java.lang.String getPisaWSDDServiceName() {
        return PisaWSDDServiceName;
    }

    public void setPisaWSDDServiceName(java.lang.String name) {
        PisaWSDDServiceName = name;
    }

    public es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisa getPisa() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Pisa_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPisa(endpoint);
    }

    public es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisa getPisa(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.registro.pisa.cliente.servicio.PisaSoapBindingStub _stub = new es.altia.agora.webservice.registro.pisa.cliente.servicio.PisaSoapBindingStub(portAddress, this);
            _stub.setPortName(getPisaWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPisaEndpointAddress(java.lang.String address) {
        Pisa_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisa.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.registro.pisa.cliente.servicio.PisaSoapBindingStub _stub = new es.altia.agora.webservice.registro.pisa.cliente.servicio.PisaSoapBindingStub(new java.net.URL(Pisa_address), this);
                _stub.setPortName(getPisaWSDDServiceName());
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
        if ("Pisa".equals(inputPortName)) {
            return getPisa();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://neptuno:8080/services/Pisa", "SWPisaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://neptuno:8080/services/Pisa", "Pisa"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Pisa".equals(portName)) {
            setPisaEndpointAddress(address);
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
