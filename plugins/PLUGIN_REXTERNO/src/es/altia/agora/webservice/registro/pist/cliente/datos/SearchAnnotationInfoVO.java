/**
 * SearchAnnotationInfoVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pist.cliente.datos;

public class SearchAnnotationInfoVO  implements java.io.Serializable {
    private java.lang.String annotationNumber;

    private java.lang.String annotationType;

    public SearchAnnotationInfoVO() {
    }

    public SearchAnnotationInfoVO(
           java.lang.String annotationNumber,
           java.lang.String annotationType) {
           this.annotationNumber = annotationNumber;
           this.annotationType = annotationType;
    }


    /**
     * Gets the annotationNumber value for this SearchAnnotationInfoVO.
     * 
     * @return annotationNumber
     */
    public java.lang.String getAnnotationNumber() {
        return annotationNumber;
    }


    /**
     * Sets the annotationNumber value for this SearchAnnotationInfoVO.
     * 
     * @param annotationNumber
     */
    public void setAnnotationNumber(java.lang.String annotationNumber) {
        this.annotationNumber = annotationNumber;
    }


    /**
     * Gets the annotationType value for this SearchAnnotationInfoVO.
     * 
     * @return annotationType
     */
    public java.lang.String getAnnotationType() {
        return annotationType;
    }


    /**
     * Sets the annotationType value for this SearchAnnotationInfoVO.
     * 
     * @param annotationType
     */
    public void setAnnotationType(java.lang.String annotationType) {
        this.annotationType = annotationType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchAnnotationInfoVO)) return false;
        SearchAnnotationInfoVO other = (SearchAnnotationInfoVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.annotationNumber==null && other.getAnnotationNumber()==null) || 
             (this.annotationNumber!=null &&
              this.annotationNumber.equals(other.getAnnotationNumber()))) &&
            ((this.annotationType==null && other.getAnnotationType()==null) || 
             (this.annotationType!=null &&
              this.annotationType.equals(other.getAnnotationType())));
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
        if (getAnnotationNumber() != null) {
            _hashCode += getAnnotationNumber().hashCode();
        }
        if (getAnnotationType() != null) {
            _hashCode += getAnnotationType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchAnnotationInfoVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "SearchAnnotationInfoVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotationNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotationNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotationType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
