/**
 * ModifyServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify;

public class ModifyServiceSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.ModifyService_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[9];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "createRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">createRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">createRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "createRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "updateRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">updateRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">updateRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "updateRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("removeRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "removeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">removeRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">removeRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "removeRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("sendRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "sendRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">sendRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">sendRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "sendRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("insertDocument");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "insertDocument"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertDocument"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocument.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertDocumentResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "insertDocumentResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteDocument");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteDocument"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteDocument"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocument.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteDocumentResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteDocumentResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("insertSigners");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "insertSigners"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertSigners"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSigners.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertSignersResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "insertSignersResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteSigners");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteSigners"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteSigners"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSigners.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteSignersResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", "deleteSignersResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[8] = oper;

    }

    public ModifyServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ModifyServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ModifyServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "enumeracionEstadoElaboracion");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "tipoDocumental");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoEstadoElaboracion");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoMetadatos");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoMetadatos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">createRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">createRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteDocument");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteDocumentResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteSigners");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSigners.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteSignersResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertDocument");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertDocumentResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertSigners");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSigners.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertSignersResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">removeRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">removeRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">sendRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">sendRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">updateRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">updateRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "action");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Action.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "actionList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Action[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "action");
            qName2 = new javax.xml.namespace.QName("", "action");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "authentication");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "comment");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Comment.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "commentList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Comment[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "comment");
            qName2 = new javax.xml.namespace.QName("", "comment");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "CsvJustificante");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.CsvJustificante.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "document");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Document.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Document[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "document");
            qName2 = new javax.xml.namespace.QName("", "document");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.DocumentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentTypeList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.DocumentType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentType");
            qName2 = new javax.xml.namespace.QName("", "documentType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJob");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJobList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJob");
            qName2 = new javax.xml.namespace.QName("", "enhancedJob");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUser");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociated");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociatedList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociated");
            qName2 = new javax.xml.namespace.QName("", "enhancedUserJobAssociated");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobInfo");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUser[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUser");
            qName2 = new javax.xml.namespace.QName("", "enhancedUser");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevel");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevelList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevel");
            qName2 = new javax.xml.namespace.QName("", "importanceLevels");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "job");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Job.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "jobList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Job[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "job");
            qName2 = new javax.xml.namespace.QName("", "job");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "MetadatoAdicional");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "metadatosEni");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.MetadatosEni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "noticeList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.State[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state");
            qName2 = new javax.xml.namespace.QName("", "state");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameter");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Parameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameterList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Parameter[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameter");
            qName2 = new javax.xml.namespace.QName("", "parameter");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "remitterList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.User[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "user");
            qName2 = new javax.xml.namespace.QName("", "user");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "request");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Request.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "requestStatus");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.RequestStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seat");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Seat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seatList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Seat[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seat");
            qName2 = new javax.xml.namespace.QName("", "seat");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signature");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signature.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signerList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer");
            qName2 = new javax.xml.namespace.QName("", "signer");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signFormat");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLine");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLine.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLineList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLine[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLine");
            qName2 = new javax.xml.namespace.QName("", "signLine");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLineType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.State.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "stateList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.State[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state");
            qName2 = new javax.xml.namespace.QName("", "state");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "stringList");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "str");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "timestampInfo");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.TimestampInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "TipoMetadatosAdicionales");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.TipoMetadatosAdicionales.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "user");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.User.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "userJob");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.UserJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "userList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.User[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "user");
            qName2 = new javax.xml.namespace.QName("", "user");
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
                    _call.setEncodingStyle(null);
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

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse createRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequest createRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "createRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse updateRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequest updateRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "updateRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {updateRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.UpdateRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse deleteRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequest deleteRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "deleteRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {deleteRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse removeRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequest removeRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "removeRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {removeRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.RemoveRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse sendRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequest sendRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "sendRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sendRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse insertDocument(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocument insertDocument) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "insertDocument"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {insertDocument});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse deleteDocument(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocument deleteDocument) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "deleteDocument"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {deleteDocument});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteDocumentResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse insertSigners(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSigners insertSigners) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "insertSigners"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {insertSigners});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse deleteSigners(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSigners deleteSigners) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "deleteSigners"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {deleteSigners});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteSignersResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
