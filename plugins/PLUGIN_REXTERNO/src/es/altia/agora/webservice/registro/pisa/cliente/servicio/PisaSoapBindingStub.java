/**
 * PisaSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pisa.cliente.servicio;

public class PisaSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisa {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SWPisa");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ParametrosBean"), es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_RetornoBean"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "SWPisaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SWPisa");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "SWPisaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

    }

    public PisaSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PisaSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PisaSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://negocio.util.aytos", "BaseBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.BaseBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_AnotacionesBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_AnotacionesBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_DocumentoBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DocumentoBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DocumentoBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_DomiciliosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DomiciliosBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_InteresadosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_InteresadosBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_InteresadosBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_TercerosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_TercerosBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "ArrayOffSWPisa_ViasBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ViasBean");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_AnotacionesBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DocumentoBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DocumentoBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DomiciliosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_InteresadosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_InteresadosBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ParametrosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_RetornoBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_TercerosBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ViasBean");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean SWPisa(int in0, es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(in0), in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String SWPisa(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
