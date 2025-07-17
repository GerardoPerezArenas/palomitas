/**
 * WSTramite.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos;

public interface WSDocumentosFlexia extends java.rmi.Remote {
    public es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento getCodigoCSV(java.lang.String in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException;
    public es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento getDocumentoByCSV(java.lang.String in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException;
    public es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante getJustificanteRegistro(es.altia.flexiaWS.documentos.bd.datos.AnotacionVO in0, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO in1) throws java.rmi.RemoteException;
    public es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO setDocumentoRegistro(es.altia.flexiaWS.documentos.bd.datos.DocumentoRegistroVO documento, es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO infoConexion) throws java.rmi.RemoteException;
}
