/**
 * InsertSigners.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request;

public class InsertSigners  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication;

    private java.lang.String requestId;

    private int signLine;

    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList;

    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType signLineType;

    public InsertSigners() {
    }

    public InsertSigners(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication,
           java.lang.String requestId,
           int signLine,
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList,
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType signLineType) {
           this.authentication = authentication;
           this.requestId = requestId;
           this.signLine = signLine;
           this.signerList = signerList;
           this.signLineType = signLineType;
    }


    /**
     * Gets the authentication value for this InsertSigners.
     * 
     * @return authentication
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication getAuthentication() {
        return authentication;
    }


    /**
     * Sets the authentication value for this InsertSigners.
     * 
     * @param authentication
     */
    public void setAuthentication(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication) {
        this.authentication = authentication;
    }


    /**
     * Gets the requestId value for this InsertSigners.
     * 
     * @return requestId
     */
    public java.lang.String getRequestId() {
        return requestId;
    }


    /**
     * Sets the requestId value for this InsertSigners.
     * 
     * @param requestId
     */
    public void setRequestId(java.lang.String requestId) {
        this.requestId = requestId;
    }


    /**
     * Gets the signLine value for this InsertSigners.
     * 
     * @return signLine
     */
    public int getSignLine() {
        return signLine;
    }


    /**
     * Sets the signLine value for this InsertSigners.
     * 
     * @param signLine
     */
    public void setSignLine(int signLine) {
        this.signLine = signLine;
    }


    /**
     * Gets the signerList value for this InsertSigners.
     * 
     * @return signerList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] getSignerList() {
        return signerList;
    }


    /**
     * Sets the signerList value for this InsertSigners.
     * 
     * @param signerList
     */
    public void setSignerList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer[] signerList) {
        this.signerList = signerList;
    }


    /**
     * Gets the signLineType value for this InsertSigners.
     * 
     * @return signLineType
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType getSignLineType() {
        return signLineType;
    }


    /**
     * Sets the signLineType value for this InsertSigners.
     * 
     * @param signLineType
     */
    public void setSignLineType(es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType signLineType) {
        this.signLineType = signLineType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InsertSigners)) return false;
        InsertSigners other = (InsertSigners) obj;
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
            this.signLine == other.getSignLine() &&
            ((this.signerList==null && other.getSignerList()==null) || 
             (this.signerList!=null &&
              java.util.Arrays.equals(this.signerList, other.getSignerList()))) &&
            ((this.signLineType==null && other.getSignLineType()==null) || 
             (this.signLineType!=null &&
              this.signLineType.equals(other.getSignLineType())));
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
        _hashCode += getSignLine();
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
        if (getSignLineType() != null) {
            _hashCode += getSignLineType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InsertSigners.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", ">insertSigners"));
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
        elemField.setFieldName("signLine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signLine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signerList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signerList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signer"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "signer"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signLineType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signLineType"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "signLineType"));
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
