/**
 * QueryEnhancedJobsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryEnhancedJobsResponse  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[] enhancedJobList;

    public QueryEnhancedJobsResponse() {
    }

    public QueryEnhancedJobsResponse(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[] enhancedJobList) {
           this.enhancedJobList = enhancedJobList;
    }


    /**
     * Gets the enhancedJobList value for this QueryEnhancedJobsResponse.
     * 
     * @return enhancedJobList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[] getEnhancedJobList() {
        return enhancedJobList;
    }


    /**
     * Sets the enhancedJobList value for this QueryEnhancedJobsResponse.
     * 
     * @param enhancedJobList
     */
    public void setEnhancedJobList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.EnhancedJob[] enhancedJobList) {
        this.enhancedJobList = enhancedJobList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEnhancedJobsResponse)) return false;
        QueryEnhancedJobsResponse other = (QueryEnhancedJobsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enhancedJobList==null && other.getEnhancedJobList()==null) || 
             (this.enhancedJobList!=null &&
              java.util.Arrays.equals(this.enhancedJobList, other.getEnhancedJobList())));
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
        if (getEnhancedJobList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEnhancedJobList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEnhancedJobList(), i);
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
        new org.apache.axis.description.TypeDesc(QueryEnhancedJobsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enhancedJobList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "enhancedJobList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "enhancedJob"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "enhancedJob"));
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
