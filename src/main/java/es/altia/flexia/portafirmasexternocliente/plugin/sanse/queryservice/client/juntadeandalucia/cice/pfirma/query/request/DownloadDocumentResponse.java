/**
 * DownloadDocumentResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class DownloadDocumentResponse  implements java.io.Serializable {
    private byte[] documentBinary;

    public DownloadDocumentResponse() {
    }

    public DownloadDocumentResponse(
           byte[] documentBinary) {
           this.documentBinary = documentBinary;
    }


    /**
     * Gets the documentBinary value for this DownloadDocumentResponse.
     * 
     * @return documentBinary
     */
    public byte[] getDocumentBinary() {
        return documentBinary;
    }


    /**
     * Sets the documentBinary value for this DownloadDocumentResponse.
     * 
     * @param documentBinary
     */
    public void setDocumentBinary(byte[] documentBinary) {
        this.documentBinary = documentBinary;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadDocumentResponse)) return false;
        DownloadDocumentResponse other = (DownloadDocumentResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.documentBinary==null && other.getDocumentBinary()==null) || 
             (this.documentBinary!=null &&
              java.util.Arrays.equals(this.documentBinary, other.getDocumentBinary())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDocumentBinary() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocumentBinary());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocumentBinary(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadDocumentResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">downloadDocumentResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentBinary");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentBinary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
