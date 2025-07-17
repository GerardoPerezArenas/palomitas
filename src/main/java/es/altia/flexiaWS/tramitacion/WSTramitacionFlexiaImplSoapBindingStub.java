/**
 * WSTramitacionFlexiaImplSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion;

public class WSTramitacionFlexiaImplSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImpl {
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
        oper.setName("finalizarTramiteOperacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "ExpedienteVO"), es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO"), es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "CondicionFinalizacionVO"), es.altia.flexiaWS.tramitacion.bd.datos.CondicionFinalizacionVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "SalidaBoolean"));
        oper.setReturnClass(es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "finalizarTramiteOperacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("iniciarExpedienteOperacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "ExpedienteVO"), es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "RespuestasTramitacionVO"));
        oper.setReturnClass(es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "iniciarExpedienteOperacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

    }

    public WSTramitacionFlexiaImplSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WSTramitacionFlexiaImplSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WSTramitacionFlexiaImplSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "CondicionFinalizacionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.CondicionFinalizacionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "DomicilioVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.DomicilioVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "ExpedienteVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "FlujoFinalizacionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InfoConexionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InteresadoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.InteresadoVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "RespuestasTramitacionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "SalidaBoolean");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://servicios.flexia", "ArrayOf_tns1_InteresadoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.InteresadoVO[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InteresadoVO");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://servicios.flexia", "ArrayOf_tns1_TramiteVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

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

    public es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean finalizarTramiteOperacion(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO in0, es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO in1, es.altia.flexiaWS.tramitacion.bd.datos.CondicionFinalizacionVO in2, java.lang.String in3, es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO in4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "finalizarTramiteOperacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1, in2, in3, in4});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO iniciarExpedienteOperacion(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO in0, es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "iniciarExpedienteOperacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
