/**
 * QueryStatesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryStatesResponse  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[] stateList;

    public QueryStatesResponse() {
    }

    public QueryStatesResponse(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[] stateList) {
           this.stateList = stateList;
    }


    /**
     * Gets the stateList value for this QueryStatesResponse.
     * 
     * @return stateList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[] getStateList() {
        return stateList;
    }


    /**
     * Sets the stateList value for this QueryStatesResponse.
     * 
     * @param stateList
     */
    public void setStateList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State[] stateList) {
        this.stateList = stateList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryStatesResponse)) return false;
        QueryStatesResponse other = (QueryStatesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.stateList==null && other.getStateList()==null) || 
             (this.stateList!=null &&
              java.util.Arrays.equals(this.stateList, other.getStateList())));
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
        if (getStateList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStateList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStateList(), i);
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
        new org.apache.axis.description.TypeDesc(QueryStatesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryStatesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stateList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stateList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "state"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "state"));
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
