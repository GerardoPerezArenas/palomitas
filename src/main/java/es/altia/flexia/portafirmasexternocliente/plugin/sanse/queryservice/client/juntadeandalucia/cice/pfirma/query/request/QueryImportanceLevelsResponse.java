/**
 * QueryImportanceLevelsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryImportanceLevelsResponse  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[] importanceLevelList;

    public QueryImportanceLevelsResponse() {
    }

    public QueryImportanceLevelsResponse(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[] importanceLevelList) {
           this.importanceLevelList = importanceLevelList;
    }


    /**
     * Gets the importanceLevelList value for this QueryImportanceLevelsResponse.
     * 
     * @return importanceLevelList
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[] getImportanceLevelList() {
        return importanceLevelList;
    }


    /**
     * Sets the importanceLevelList value for this QueryImportanceLevelsResponse.
     * 
     * @param importanceLevelList
     */
    public void setImportanceLevelList(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel[] importanceLevelList) {
        this.importanceLevelList = importanceLevelList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryImportanceLevelsResponse)) return false;
        QueryImportanceLevelsResponse other = (QueryImportanceLevelsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.importanceLevelList==null && other.getImportanceLevelList()==null) || 
             (this.importanceLevelList!=null &&
              java.util.Arrays.equals(this.importanceLevelList, other.getImportanceLevelList())));
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
        if (getImportanceLevelList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getImportanceLevelList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getImportanceLevelList(), i);
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
        new org.apache.axis.description.TypeDesc(QueryImportanceLevelsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryImportanceLevelsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importanceLevelList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importanceLevelList"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "importanceLevel"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "importanceLevels"));
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
