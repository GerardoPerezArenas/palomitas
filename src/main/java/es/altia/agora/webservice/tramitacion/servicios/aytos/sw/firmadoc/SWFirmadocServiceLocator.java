/**
 * SWFirmadocServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc;

public class SWFirmadocServiceLocator extends org.apache.axis.client.Service implements es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadocService {

    public SWFirmadocServiceLocator() {
    }


    public SWFirmadocServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SWFirmadocServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for firmadoc
    private java.lang.String firmadoc_address = "http://net.sicalwin.com:9080/services/firmadoc";

    public java.lang.String getfirmadocAddress() {
        return firmadoc_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String firmadocWSDDServiceName = "firmadoc";

    public java.lang.String getfirmadocWSDDServiceName() {
        return firmadocWSDDServiceName;
    }

    public void setfirmadocWSDDServiceName(java.lang.String name) {
        firmadocWSDDServiceName = name;
    }

    public es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc getfirmadoc() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(firmadoc_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getfirmadoc(endpoint);
    }

    public es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc getfirmadoc(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub _stub = new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub(portAddress, this);
            _stub.setPortName(getfirmadocWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setfirmadocEndpointAddress(java.lang.String address) {
        firmadoc_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc.class.isAssignableFrom(serviceEndpointInterface)) {
                es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub _stub = new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub(new java.net.URL(firmadoc_address), this);
                _stub.setPortName(getfirmadocWSDDServiceName());
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
        if ("firmadoc".equals(inputPortName)) {
            return getfirmadoc();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://net.sicalwin.com:9080/services/firmadoc", "SWFirmadocService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://net.sicalwin.com:9080/services/firmadoc", "firmadoc"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("firmadoc".equals(portName)) {
            setfirmadocEndpointAddress(address);
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
