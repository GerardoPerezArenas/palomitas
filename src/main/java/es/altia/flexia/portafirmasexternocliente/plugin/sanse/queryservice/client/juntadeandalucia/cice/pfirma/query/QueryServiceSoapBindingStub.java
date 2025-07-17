/**
 * QueryServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query;

public class QueryServiceSoapBindingStub extends org.apache.axis.client.Stub implements es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.QueryService_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[15];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("downloadSign");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "downloadSign"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadSign"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSign.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadSignResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "downloadSignResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryUsers"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryUsers"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsers.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryUsersResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryUsersResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryDocumentTypes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryDocumentTypes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryDocumentTypes"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypes.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryDocumentTypesResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryDocumentTypesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("downloadDocument");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "downloadDocument"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadDocument"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocument.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadDocumentResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "downloadDocumentResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryStates");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryStates"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryStates"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStates.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryStatesResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryStatesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryImportanceLevels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryImportanceLevels"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryImportanceLevels"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevels.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryImportanceLevelsResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryImportanceLevelsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryJobs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryJobs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryJobs"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobs.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryJobsResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryJobsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryRequest");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryRequest"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryRequestResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryRequestResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getCVS");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "getCVS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">getCVS"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">getCVSResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "getCVSResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryEnhancedUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUsers"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUsers"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsers.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUsersResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUsersResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryEnhancedJobs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedJobs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobs"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobs.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobsResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedJobsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("querySeats");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "querySeats"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">querySeats"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeats.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">querySeatsResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "querySeatsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryEnhancedUserJobAssociatedToJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUserJobAssociatedToJob"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToJob"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJob.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToJobResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUserJobAssociatedToJobResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryEnhancedUserJobAssociatedToUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUserJobAssociatedToUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToUser"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUser.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToUserResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryEnhancedUserJobAssociatedToUserResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryCSVyJustificante");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryCSVyJustificante"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryCSVyJustificante"), es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificante.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryCSVyJustificanteResponse"));
        oper.setReturnClass(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "queryCSVyJustificanteResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "exceptionInfo"),
                      "es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo",
                      new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo"), 
                      true
                     ));
        _operations[14] = oper;

    }

    public QueryServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public QueryServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public QueryServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "tipoDocumental");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoEstadoElaboracion");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoMetadatos");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoMetadatos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadDocument");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadDocumentResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadSign");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSign.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadSignResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">getCVS");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">getCVSResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryCSVyJustificante");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificante.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryCSVyJustificanteResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryDocumentTypes");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryDocumentTypesResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobs");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobsResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToJob");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToJobResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToUser");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToUserResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUsers");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsers.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUsersResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryImportanceLevels");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevels.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryImportanceLevelsResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryJobs");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryJobsResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryRequest");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryRequestResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">querySeats");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeats.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">querySeatsResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryStates");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStates.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryStatesResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryUsers");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsers.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryUsersResponse");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", ">exceptionInfo");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "action");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Action.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "actionList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Action[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "action");
            qName2 = new javax.xml.namespace.QName("", "action");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "authentication");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "comment");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Comment.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "commentList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Comment[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "comment");
            qName2 = new javax.xml.namespace.QName("", "comment");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "CsvJustificante");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.CsvJustificante.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "document");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Document.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Document[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "document");
            qName2 = new javax.xml.namespace.QName("", "document");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.DocumentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentTypeList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.DocumentType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "documentType");
            qName2 = new javax.xml.namespace.QName("", "documentType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJob");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJobList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJob");
            qName2 = new javax.xml.namespace.QName("", "enhancedJob");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUser");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociated");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociatedList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociated");
            qName2 = new javax.xml.namespace.QName("", "enhancedUserJobAssociated");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobInfo");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUser[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUser");
            qName2 = new javax.xml.namespace.QName("", "enhancedUser");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevel");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevelList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevel");
            qName2 = new javax.xml.namespace.QName("", "importanceLevels");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "job");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Job.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "jobList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Job[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "job");
            qName2 = new javax.xml.namespace.QName("", "job");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "MetadatoAdicional");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "metadatosEni");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatosEni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "noticeList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state");
            qName2 = new javax.xml.namespace.QName("", "state");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameter");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Parameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameterList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Parameter[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "parameter");
            qName2 = new javax.xml.namespace.QName("", "parameter");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "remitterList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.User[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "user");
            qName2 = new javax.xml.namespace.QName("", "user");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "request");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Request.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "requestStatus");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.RequestStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seat");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Seat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seatList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Seat[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "seat");
            qName2 = new javax.xml.namespace.QName("", "seat");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signature");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signature.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signerList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signer[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer");
            qName2 = new javax.xml.namespace.QName("", "signer");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signFormat");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLine");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignLine.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLineList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignLine[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLine");
            qName2 = new javax.xml.namespace.QName("", "signLine");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLineType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignLineType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signType");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "stateList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[].class;
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
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.TimestampInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "TipoMetadatosAdicionales");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.TipoMetadatosAdicionales.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "user");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.User.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "userJob");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.UserJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "userList");
            cachedSerQNames.add(qName);
            cls = es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.User[].class;
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

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse downloadSign(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSign downloadSign) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "downloadSign"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {downloadSign});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse queryUsers(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsers queryUsers) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryUsers});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse queryDocumentTypes(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypes queryDocumentTypes) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryDocumentTypes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryDocumentTypes});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryDocumentTypesResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse downloadDocument(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocument downloadDocument) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "downloadDocument"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {downloadDocument});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse queryStates(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStates queryStates) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryStates"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryStates});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryStatesResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse queryImportanceLevels(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevels queryImportanceLevels) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryImportanceLevels"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryImportanceLevels});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryImportanceLevelsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse queryJobs(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobs queryJobs) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryJobs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryJobsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse queryRequest(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequest queryRequest) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "queryRequest"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse getCVS(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVS getCVS) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
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
        _call.setOperationName(new javax.xml.namespace.QName("", "getCVS"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getCVS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.GetCVSResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse queryEnhancedUsers(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsers queryEnhancedUsers) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "queryEnhancedUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryEnhancedUsers});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUsersResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse queryEnhancedJobs(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobs queryEnhancedJobs) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "queryEnhancedJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryEnhancedJobs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedJobsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse querySeats(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeats querySeats) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "querySeats"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {querySeats});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QuerySeatsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse queryEnhancedUserJobAssociatedToJob(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJob queryEnhancedUserJobAssociatedToJob) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "queryEnhancedUserJobAssociatedToJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryEnhancedUserJobAssociatedToJob});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToJobResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse queryEnhancedUserJobAssociatedToUser(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUser queryEnhancedUserJobAssociatedToUser) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "queryEnhancedUserJobAssociatedToUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryEnhancedUserJobAssociatedToUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryEnhancedUserJobAssociatedToUserResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse queryCSVyJustificante(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificante queryCSVyJustificante) throws java.rmi.RemoteException, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "queryCSVyJustificante"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryCSVyJustificante});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse) org.apache.axis.utils.JavaUtils.convert(_resp, es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryCSVyJustificanteResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) {
              throw (es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
