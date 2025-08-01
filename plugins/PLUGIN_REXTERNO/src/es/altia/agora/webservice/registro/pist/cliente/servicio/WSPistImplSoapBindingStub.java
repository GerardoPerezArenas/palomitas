/**
 * WSPistImplSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pist.cliente.servicio;

public class WSPistImplSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImpl {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[5];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAnnotationInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getAnnotationInfoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("manageAnnotations");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "manageAnnotationsReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAnnotationsByNumbers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "searchInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "ArrayOf_tns1_SearchAnnotationInfoVO"), es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getAnnotationsByNumbersReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAnnotationInterested");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "searchInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "SearchAnnotationInfoVO"), es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getAnnotationInterestedReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAnnotations");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "soonerDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "laterDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "state"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "groupingNames"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "ArrayOf_xsd_string"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "annotationType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        oper.setReturnClass(es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getAnnotationsReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

    }

    public WSPistImplSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WSPistImplSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WSPistImplSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationInfoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationPersonInfoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "SearchAnnotationInfoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "ArrayOf_tns1_AnnotationInfoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationInfoVO");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "ArrayOf_tns1_SearchAnnotationInfoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "SearchAnnotationInfoVO");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "ArrayOf_xsd_string");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
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

    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationInfo(java.lang.String annotationNumber, java.lang.String annotationType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "getAnnotationInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {annotationNumber, annotationType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO manageAnnotations(java.lang.String annotationNumber, java.lang.String annotationType, java.lang.String annotationState) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "manageAnnotations"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {annotationNumber, annotationType, annotationState});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationsByNumbers(es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO[] searchInfo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "getAnnotationsByNumbers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {searchInfo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationInterested(es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO searchInfo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "getAnnotationInterested"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {searchInfo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotations(java.util.Calendar soonerDate, java.util.Calendar laterDate, java.lang.String state, java.lang.String[] groupingNames, java.lang.String annotationType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://service.pist.ws.altia.es", "getAnnotations"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {soonerDate, laterDate, state, groupingNames, annotationType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
