/**
 * QueryEnhancedUserJobAssociatedToJobResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryEnhancedUserJobAssociatedToJobResponse  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[] enhancedUserJobAssociatedList;

    public QueryEnhancedUserJobAssociatedToJobResponse() {
    }

    public QueryEnhancedUserJobAssociatedToJobResponse(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[] enhancedUserJobAssociatedList) {
           this.enhancedUserJobAssociatedList = enhancedUserJobAssociatedList;
    }


    /**
     * Gets the enhancedUserJobAssociatedList value for this QueryEnhancedUserJobAssociatedToJobResponse.
     * 
     * @return enhancedUserJobAssociatedList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[] getEnhancedUserJobAssociatedList() {
        return enhancedUserJobAssociatedList;
    }


    /**
     * Sets the enhancedUserJobAssociatedList value for this QueryEnhancedUserJobAssociatedToJobResponse.
     * 
     * @param enhancedUserJobAssociatedList
     */
    public void setEnhancedUserJobAssociatedList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedUserJobAssociated[] enhancedUserJobAssociatedList) {
        this.enhancedUserJobAssociatedList = enhancedUserJobAssociatedList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEnhancedUserJobAssociatedToJobResponse)) return false;
        QueryEnhancedUserJobAssociatedToJobResponse other = (QueryEnhancedUserJobAssociatedToJobResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enhancedUserJobAssociatedList==null && other.getEnhancedUserJobAssociatedList()==null) || 
             (this.enhancedUserJobAssociatedList!=null &&
              java.util.Arrays.equals(this.enhancedUserJobAssociatedList, other.getEnhancedUserJobAssociatedList())));
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
        if (getEnhancedUserJobAssociatedList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEnhancedUserJobAssociatedList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEnhancedUserJobAssociatedList(), i);
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
        new org.apache.axis.description.TypeDesc(QueryEnhancedUserJobAssociatedToJobResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUserJobAssociatedToJobResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enhancedUserJobAssociatedList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "enhancedUserJobAssociatedList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedUserJobAssociated"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "enhancedUserJobAssociated"));
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
