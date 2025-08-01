/**
 * WSDocumentosFlexiaImplSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos;

public class WSDocumentosFlexiaImplSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.flexiaWS.documentos.WSDocumentosFlexiaImpl {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDocumentoByCSV");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaFicheroDocumento"));
        oper.setReturnClass(es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getDocumentoByCSVReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setDocumentoRegistro");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "DocumentoRegistroVO"), es.altia.flexiaWS.documentos.bd.datos.DocumentoRegistroVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "EstadoOperacionVO"));
        oper.setReturnClass(es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "setDocumentoRegistroReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getJustificanteRegistro");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "AnotacionVO"), es.altia.flexiaWS.documentos.bd.datos.AnotacionVO.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaJustificante"));
        oper.setReturnClass(es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getJustificanteRegistroReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getCodigoCSV");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InfoConexionVO"), es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaCodigoCSV"));
        oper.setReturnClass(es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getCodigoCSVReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

    }

    public WSDocumentosFlexiaImplSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WSDocumentosFlexiaImplSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WSDocumentosFlexiaImplSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "AnotacionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.AnotacionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "DocumentoRegistroVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.DocumentoRegistroVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "EstadoOperacionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "FicheroDocumentoVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InfoConexionVO");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaCodigoCSV");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaFicheroDocumento");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaJustificante");
            cachedSerQNames.add(qName);
            cls = es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante.class;
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

    public es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento getDocumentoByCSV(java.lang.String in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "getDocumentoByCSV"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO setDocumentoRegistro(es.altia.flexiaWS.documentos.bd.datos.DocumentoRegistroVO in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "setDocumentoRegistro"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante getJustificanteRegistro(es.altia.flexiaWS.documentos.bd.datos.AnotacionVO in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "getJustificanteRegistro"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV getCodigoCSV(java.lang.String in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://servicios.flexia", "getCodigoCSV"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
