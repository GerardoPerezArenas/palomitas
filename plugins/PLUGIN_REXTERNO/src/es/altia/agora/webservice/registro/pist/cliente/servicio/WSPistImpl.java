/**
 * WSPistImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pist.cliente.servicio;

public interface WSPistImpl extends java.rmi.Remote {
    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationInfo(java.lang.String annotationNumber, java.lang.String annotationType) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO manageAnnotations(java.lang.String annotationNumber, java.lang.String annotationType, java.lang.String annotationState) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationsByNumbers(es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO[] searchInfo) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotationInterested(es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO searchInfo) throws java.rmi.RemoteException;
    public es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO getAnnotations(java.util.Calendar soonerDate, java.util.Calendar laterDate, java.lang.String state, java.lang.String[] groupingNames, java.lang.String annotationType) throws java.rmi.RemoteException;
}
