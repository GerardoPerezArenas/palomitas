/**
 * DeleteSigners.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request;

public class DeleteSigners  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication;

    private java.lang.String requestId;

    private java.lang.Integer signLineNumber;

    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList;

    public DeleteSigners() {
    }

    public DeleteSigners(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication,
           java.lang.String requestId,
           java.lang.Integer signLineNumber,
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList) {
           this.authentication = authentication;
           this.requestId = requestId;
           this.signLineNumber = signLineNumber;
           this.signerList = signerList;
    }


    /**
     * Gets the authentication value for this DeleteSigners.
     * 
     * @return authentication
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication getAuthentication() {
        return authentication;
    }


    /**
     * Sets the authentication value for this DeleteSigners.
     * 
     * @param authentication
     */
    public void setAuthentication(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication) {
        this.authentication = authentication;
    }


    /**
     * Gets the requestId value for this DeleteSigners.
     * 
     * @return requestId
     */
    public java.lang.String getRequestId() {
        return requestId;
    }


    /**
     * Sets the requestId value for this DeleteSigners.
     * 
     * @param requestId
     */
    public void setRequestId(java.lang.String requestId) {
        this.requestId = requestId;
    }


    /**
     * Gets the signLineNumber value for this DeleteSigners.
     * 
     * @return signLineNumber
     */
    public java.lang.Integer getSignLineNumber() {
        return signLineNumber;
    }


    /**
     * Sets the signLineNumber value for this DeleteSigners.
     * 
     * @param signLineNumber
     */
    public void setSignLineNumber(java.lang.Integer signLineNumber) {
        this.signLineNumber = signLineNumber;
    }


    /**
     * Gets the signerList value for this DeleteSigners.
     * 
     * @return signerList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] getSignerList() {
        return signerList;
    }


    /**
     * Sets the signerList value for this DeleteSigners.
     * 
     * @param signerList
     */
    public void setSignerList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList) {
        this.signerList = signerList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeleteSigners)) return false;
        DeleteSigners other = (DeleteSigners) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.authentication==null && other.getAuthentication()==null) || 
             (this.authentication!=null &&
              this.authentication.equals(other.getAuthentication()))) &&
            ((this.requestId==null && other.getRequestId()==null) || 
             (this.requestId!=null &&
              this.requestId.equals(other.getRequestId()))) &&
            ((this.signLineNumber==null && other.getSignLineNumber()==null) || 
             (this.signLineNumber!=null &&
              this.signLineNumber.equals(other.getSignLineNumber()))) &&
            ((this.signerList==null && other.getSignerList()==null) || 
             (this.signerList!=null &&
              java.util.Arrays.equals(this.signerList, other.getSignerList())));
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
        if (getAuthentication() != null) {
            _hashCode += getAuthentication().hashCode();
        }
        if (getRequestId() != null) {
            _hashCode += getRequestId().hashCode();
        }
        if (getSignLineNumber() != null) {
            _hashCode += getSignLineNumber().hashCode();
        }
        if (getSignerList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSignerList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSignerList(), i);
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
        new org.apache.axis.description.TypeDesc(DeleteSigners.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">deleteSigners"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authentication");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authentication"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "authentication"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "requestId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signLineNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signLineNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signerList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signerList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "signer"));
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
